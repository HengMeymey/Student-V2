////package students.student_management.spring_web.aspect;
////
////import org.aspectj.lang.JoinPoint;
////import org.aspectj.lang.annotation.*;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.stereotype.Component;
////
////@Component
////@Aspect
////public class LoggingAspect {
////
////    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
////
////    @Pointcut("execution(* students.student_management.spring_web.controller..*(..))")
////    public void controllerMethods() {}
////
////    @Before("controllerMethods()")
////    public void logBefore(JoinPoint joinPoint) {
////        logger.info("Entering method: {}", joinPoint.getSignature().toShortString());
////    }
////
////    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
////    public void logAfterReturning(JoinPoint joinPoint, Object result) {
////        logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
////    }
////}
//
//package students.student_management.spring_web.aspect;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.util.Arrays;
//
//@Component
//@Aspect
//public class LoggingAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Pointcut("execution(* students.student_management.spring_web.controller..*(..))")
//    public void controllerMethods() {}
//
//    @Before("controllerMethods()")
//    public void logBefore(JoinPoint joinPoint) {
//        // Get current HTTP request
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        // Get user information from the SecurityContext
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = "anonymousUser";
//
//        // Check if the authentication is present and the user is authenticated
//        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
//            username = authentication.getName();  // Get the username from the Authentication object
//        }
//
//        // Log the request details
//        logger.info("Incoming request: {} {} by User: {}", request.getMethod(), request.getRequestURI(), username);
//        logger.info("Request Arguments: {}", Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
//    public void logAfterReturning(JoinPoint joinPoint, Object result) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
//        logger.info("Response for {} {}: {}", request.getMethod(), request.getRequestURI(), result);
//    }
//
//    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        logger.error("Exception in {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
//    }
//}

//package students.student_management.spring_web.aspect;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Aspect
//@Component
//public class LoggingAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Around("execution(* students.student_management.spring_web.controller.*.*(..))")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        String methodName = joinPoint.getSignature().getName();
//        String requestURI = ""; // You may need to extract the request URI
//        String userName = SecurityContextHolder.getContext().getAuthentication() != null
//                ? SecurityContextHolder.getContext().getAuthentication().getName()
//                : "Anonymous";
//
//        // Log incoming request
//        logger.info("Request: {} by User: {}", methodName, userName);
//
//        Object result = joinPoint.proceed();
//
//        // Log the response
//        if (result instanceof ResponseEntity<?>) {
//            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
//            Object responseBody = responseEntity.getBody();
//
//            // Check if response body is of type Map
//            if (responseBody instanceof Map<?, ?>) {
//                Map<?, ?> responseMap = (Map<?, ?>) responseBody;
//
//                // Convert data to JSON string
//                String dataAsJson = "";
//                if (responseMap.get("data") != null) {
//                    try {
//                        dataAsJson = objectMapper.writeValueAsString(responseMap.get("data"));
//                    } catch (JsonProcessingException e) {
//                        logger.error("Failed to convert data to JSON: {}", e.getMessage());
//                    }
//                }
//
//                logger.info("Response for {}: <{} {}>, {{message={}, status={}}}, data={}",
//                        requestURI,
//                        responseEntity.getStatusCodeValue(),
//                        responseEntity.getStatusCode(),
//                        responseMap.get("message"),
//                        responseMap.get("status"),
//                        dataAsJson);
//            } else {
//                logger.info("Response for {}: <{} {}>, body={}",
//                        requestURI,
//                        responseEntity.getStatusCodeValue(),
//                        responseEntity.getStatusCode(),
//                        responseBody);
//            }
//        } else {
//            logger.info("Exiting method: {} with result: {}", methodName, result);
//        }
//
//        return result;
//    }
//}

//package students.student_management.spring_web.aspect;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Map;
//
//@Aspect
//@Component
//public class LoggingAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
//
//    @Around("execution(* students.student_management.spring_web.controller.*.*(..)) && !execution(* students.student_management.spring_web.controller.AuthController.*(..))")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        // Get the HTTP request from the current context
//        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attrs.getRequest();
//        String requestURI = request.getRequestURI(); // Get the request URI
//        String userName = SecurityContextHolder.getContext().getAuthentication() != null
//                ? SecurityContextHolder.getContext().getAuthentication().getName()
//                : "Anonymous";
//
//        // Log incoming request
//        logger.info("Request: {} by User: {}", requestURI, userName);
//
//        Object result = joinPoint.proceed();
//
//        // Log the response
//        if (result instanceof ResponseEntity<?>) {
//            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
//            Object responseBody = responseEntity.getBody();
//
//            // Check if response body is of type Map
//            if (responseBody instanceof Map<?, ?>) {
//                Map<?, ?> responseMap = (Map<?, ?>) responseBody;
//
//                // Convert data to JSON string
//                String dataAsJson = "";
//                if (responseMap.get("data") != null) {
//                    try {
//                        dataAsJson = objectMapper.writeValueAsString(responseMap.get("data"));
//                    } catch (JsonProcessingException e) {
//                        logger.error("Failed to convert data to JSON: {}", e.getMessage());
//                    }
//                }
//
//                logger.info("Response for {}: <{} {}>, {{message={}, status={}}}, data={}",
//                        requestURI,
//                        responseEntity.getStatusCodeValue(),
//                        responseEntity.getStatusCode(),
//                        responseMap.get("message"),
//                        responseMap.get("status"),
//                        dataAsJson);
//            } else {
//                logger.info("Response for {}: <{} {}>, body={}",
//                        requestURI,
//                        responseEntity.getStatusCodeValue(),
//                        responseEntity.getStatusCode(),
//                        responseBody);
//            }
//        } else {
//            logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().getName(), result);
//        }
//
//        return result;
//    }
//}

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
