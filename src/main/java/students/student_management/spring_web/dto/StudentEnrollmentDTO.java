package students.student_management.spring_web.dto;

import java.util.Date;

public class StudentEnrollmentDTO {
    private Long studentId;
    private String studentName;
    private String contact;
    private Date dob;
    private String gender;
    private String studentStatus;
    private String department;
    private String courseClassName;
    private String courseName;
    private float startTime;
    private float endTime;
    private Date enrollmentDate;
    private int year;

    public StudentEnrollmentDTO(Long studentId, String studentName, String contact, Date dob, String gender, String studentStatus, String department, String courseClassName, String courseName, float startTime, float endTime, Date enrollmentDate, int year) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.contact = contact;
        this.dob = dob;
        this.gender = gender;
        this.studentStatus = studentStatus;
        this.department = department;
        this.courseClassName = courseClassName;
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enrollmentDate = enrollmentDate;
        this.year = year;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}