package students.student_management.spring_web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.student_management.spring_web.model.Teacher;
import students.student_management.spring_web.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity.ok(teacherService.saveTeacher(teacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        Teacher existingTeacher = teacherService.getTeacherById(id);
        if (existingTeacher != null) {
            existingTeacher.setName(teacherDetails.getName());
            existingTeacher.setContact(teacherDetails.getContact());
            existingTeacher.setHireDate(teacherDetails.getHireDate());
            existingTeacher.setSubjectSpecialization(teacherDetails.getSubjectSpecialization());
            existingTeacher.setIsEmployed(teacherDetails.getIsEmployed());
            existingTeacher.setDepartment(teacherDetails.getDepartment());

            Teacher updatedTeacher = teacherService.saveTeacher(existingTeacher);
            return ResponseEntity.ok(updatedTeacher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
