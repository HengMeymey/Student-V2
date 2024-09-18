package students.student_management.spring_web.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    // Pointcut to match all methods in controllers except createUser and loginUser
    @Around("execution(* students.student_management.spring_web.controller.*.*(..)) && !execution(* students.student_management.spring_web.controller.AuthController.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the HTTP request from the current context
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String requestURI = request.getRequestURI(); // Get the request URI
        String userName = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "Anonymous";

        // Log incoming request
        logger.info("Request: {} by User: {}", requestURI, userName);

        Object result = joinPoint.proceed();

        // Log the response
        if (result instanceof ResponseEntity<?>) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object responseBody = responseEntity.getBody();

            // Check if response body is of type Map
            if (responseBody instanceof Map<?, ?>) {
                Map<?, ?> responseMap = (Map<?, ?>) responseBody;

                // Convert data to JSON string
                String dataAsJson = "";
                if (responseMap.get("data") != null) {
                    try {
                        dataAsJson = objectMapper.writeValueAsString(responseMap.get("data"));
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to convert data to JSON: {}", e.getMessage());
                    }
                }

                logger.info("Response for {}: <{} {}>, {{message={}, status={}}}, data={}",
                        requestURI,
                        responseEntity.getStatusCodeValue(),
                        responseEntity.getStatusCode(),
                        responseMap.get("message"),
                        responseMap.get("status"),
                        dataAsJson);
            } else {
                logger.info("Response for {}: <{} {}>, body={}",
                        requestURI,
                        responseEntity.getStatusCodeValue(),
                        responseEntity.getStatusCode(),
                        responseBody);
            }
        }

        return result;
    }
}
