package students.student_management.spring_web.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PhoneVerificationResponse {
    private boolean valid;
    private String number;
    private String localFormat;
    private String internationalFormat;
    private String countryName;
}
