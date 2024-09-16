package students.student_management.spring_web.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* students.student_management.spring_web.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: {}", joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }
}