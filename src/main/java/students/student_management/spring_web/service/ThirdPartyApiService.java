package students.student_management.spring_web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import students.student_management.spring_web.model.PhoneVerificationResponse;
@Service
public class ThirdPartyApiService {

    private final RestTemplate restTemplate;

    @Value("${numverify.api.key}")
    private String apiKey;

    @Value("${numverify.api.url}")
    private String apiUrl;

    public ThirdPartyApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PhoneVerificationResponse verifyPhoneNumber(String phoneNumber) {
        String url = apiUrl + "?access_key=" + apiKey + "&number=" + phoneNumber;
        System.out.println("Calling API: " + url);

        try {
            ResponseEntity<PhoneVerificationResponse> response = restTemplate.getForEntity(url, PhoneVerificationResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to verify phone number, status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while calling third-party API: " + e.getMessage(), e);
        }
    }
}