package com.dealerapp.facebook.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.dealerapp.facebook.controller.*.*(..))")
    public void logRequest(JoinPoint joinPoint) {
        log.info("Calling: {} with args {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.dealerapp.facebook.controller.*.*(..))", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        log.info("Completed: {} with response {}", joinPoint.getSignature(), result);
    }
}
