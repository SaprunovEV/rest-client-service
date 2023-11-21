package by.sapra.restclientservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before execution of: {}", joinPoint.getSignature().getName());
    }

    @After("@annotation(Loggable)")
    public void logAfter(JoinPoint joinPoint) {
        log.info("After execution of: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(Loggable)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("After returning from: {} with result: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "@annotation(Loggable)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.info("After throeing from: {} with exception: {}", joinPoint.getSignature().getName(), exception);
    }

    @Around("@annotation(Loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Around method: {} is called", joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("Around method: {} is finished", joinPoint.getSignature().getName());

        return proceed;
    }
}
