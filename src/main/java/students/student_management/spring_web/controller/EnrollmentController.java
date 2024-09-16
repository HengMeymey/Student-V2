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

    @PostMapping
    public ResponseEntity<?> createEnrollment(@Valid @RequestBody Enrollment enrollment) {
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createSuccessResponse("Enrollment created successfully.", savedEnrollment));
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long id) {
        try {
            Enrollment enrollment = enrollmentService.getAllEnrollments().stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));
            return ResponseEntity.ok(enrollment); // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage())); // 404 Not Found
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnrollment(@PathVariable Long id, @Valid @RequestBody Enrollment updatedEnrollment) {
        try {
            Enrollment enrollment = enrollmentService.updateEnrollment(id, updatedEnrollment);
            return ResponseEntity.ok(createSuccessResponse("Enrollment updated successfully.", enrollment)); // 200 OK
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(ex.getMessage())); // 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.ok(createSuccessResponse("Enrollment deleted successfully.", null)); // 200 OK
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(ex.getMessage())); // 404 Not Found
        }
    }

    // Helper method to create success response
    private Map<String, Object> createSuccessResponse(String message, Enrollment enrollment) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (enrollment != null) {
            response.put("enrollment", enrollment);
        }
        return response;
    }

    // Helper method to create error response
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        return errorResponse;
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentEnrollmentDTO>> getAllStudentEnrollments() {
        List<StudentEnrollmentDTO> studentEnrollments = enrollmentService.getAllStudentEnrollments();
        return ResponseEntity.ok(studentEnrollments); // 200 OK
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