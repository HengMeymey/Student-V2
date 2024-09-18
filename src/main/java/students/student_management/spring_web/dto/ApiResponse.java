package students.student_management.spring_web.dto;

import java.util.List;

public class ApiResponse<T> {
    private String message;
    private String status;
    private List<T> data;

    public ApiResponse(String message, String status, List<T> data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
