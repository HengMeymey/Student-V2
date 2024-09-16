package students.student_management.spring_web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.exception.ResourceNotFoundException;
import students.student_management.spring_web.model.Department;
import students.student_management.spring_web.model.StudentStatus;
import students.student_management.spring_web.service.DepartmentExcelExportService;
import students.student_management.spring_web.service.DepartmentService;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentExcelExportService excelExportService; // Add this line

    public DepartmentController(DepartmentService departmentService, DepartmentExcelExportService excelExportService) {
        this.departmentService = departmentService;
        this.excelExportService = excelExportService; // Add this line
    }
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        return ResponseEntity.ok(department);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDepartment(@Valid @RequestBody Department department) {
        try {
            Department createdDepartment = departmentService.saveDepartment(department);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Department has been created successfully!");
            response.put("status", createdDepartment);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to create department: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department department) {
//        Department updatedDepartment = departmentService.updateDepartment(id, department);
//        return ResponseEntity.ok(updatedDepartment);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department department) {
        try {
            Department updatedDepartment = departmentService.updateDepartment(id, department);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Department has been updated successfully!");
            response.put("status", updatedDepartment);

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to department: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentService.deleteDepartment(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Department has been deleted successfully !!");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportDepartmentsToExcel() {
        List<Department> departments = departmentService.getAllDepartments();
        ByteArrayOutputStream outputStream = excelExportService.exportDepartmentsToExcel(departments);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=departments.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(outputStream.toByteArray());
    }
}