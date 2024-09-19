package students.student_management.spring_web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.dto.StudentEnrollmentDTO;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Enrollment;
import students.student_management.spring_web.service.EnrollmentService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEnrollments() {
        Map<String, Object> response = new HashMap<>();
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        if (enrollments.isEmpty()) {
            response.put("message", "No enrollment to display");
            response.put("status", "SUCCESS");
            response.put("data", enrollments);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Enrollments retrieved successfully.");
        response.put("status", "SUCCESS");
        response.put("data", enrollments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEnrollmentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);
            response.put("message", "Enrollment retrieved successfully.");
            response.put("status", "SUCCESS");
            response.put("data", enrollment);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEnrollment(@Valid @RequestBody Enrollment enrollment) {
        Map<String, Object> response = new HashMap<>();
        try {
            Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);
            response.put("message", "Enrollment created successfully!");
            response.put("status", "SUCCESS");
            response.put("data", savedEnrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to create enrollment: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEnrollment(@PathVariable Long id, @Valid @RequestBody Enrollment updatedEnrollment) {
        Map<String, Object> response = new HashMap<>();
        try {
            Enrollment existingEnrollment = enrollmentService.getEnrollmentById(id);
            existingEnrollment.setStudent(updatedEnrollment.getStudent());
            existingEnrollment.setCourseClass(updatedEnrollment.getCourseClass());
            existingEnrollment.setEnrollmentDate(updatedEnrollment.getEnrollmentDate());
            existingEnrollment.setYear(updatedEnrollment.getYear());

            Enrollment updateEnrollment = enrollmentService.updateEnrollment(id,updatedEnrollment);
            response.put("message", "Enrollment updated successfully!");
            response.put("status", "SUCCESS");
            response.put("data", updateEnrollment);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to update enrollment: " + e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEnrollment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            enrollmentService.deleteEnrollment(id);
            response.put("message", "Enrollment deleted successfully.");
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "FAIL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentEnrollmentDTO>> getAllStudentEnrollments() {
        List<StudentEnrollmentDTO> enrollments = enrollmentService.getAllStudentEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/students/export/excel")
    public ResponseEntity<byte[]> exportToExcel(HttpServletResponse response) throws IOException {
        List<StudentEnrollmentDTO> enrollments = enrollmentService.getAllStudentEnrollments();

        // Create a workbook and a sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Enrollments");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "StudentId", "StudentName", "Contact", "Date of Birth", "Gender",
                "Status", "Department", "ClassName", "CourseName",
                "StartTime", "EndTime", "EnrollmentDate", "Year"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Populate data rows
        int rowNum = 1;
        for (StudentEnrollmentDTO enrollment : enrollments) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(enrollment.getStudentId());
            row.createCell(1).setCellValue(enrollment.getStudentName());
            row.createCell(2).setCellValue(enrollment.getContact());
            row.createCell(3).setCellValue(enrollment.getDob().toString());
            row.createCell(4).setCellValue(enrollment.getGender());
            row.createCell(5).setCellValue(enrollment.getStudentStatus());
            row.createCell(6).setCellValue(enrollment.getDepartment());
            row.createCell(7).setCellValue(enrollment.getCourseClassName());
            row.createCell(8).setCellValue(enrollment.getCourseName());
            row.createCell(9).setCellValue(enrollment.getStartTime());
            row.createCell(10).setCellValue(enrollment.getEndTime());
            row.createCell(11).setCellValue(enrollment.getEnrollmentDate().toString());
            row.createCell(12).setCellValue(enrollment.getYear());
        }

        // Set the content type for Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // Set headers for file download
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_enrollments.xlsx");

        // Write the workbook to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Return the output stream as a byte array response
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_enrollments.xlsx")
                .body(outputStream.toByteArray());
    }
}