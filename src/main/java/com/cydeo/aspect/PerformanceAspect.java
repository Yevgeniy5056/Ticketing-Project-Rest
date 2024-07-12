package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    public void executionTimePointCut() {
    }

    @Around("executionTimePointCut()")
    public Object aroundAnyExecutionTimeAdvice(ProceedingJoinPoint proceedingJoinPoint) {

        long startTime = System.currentTimeMillis();

        Object result = null;

        log.info("Execution starts:");

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        log.info("Time taken to execute: {} ms - Method: {}"
                , (endTime - startTime)
                , proceedingJoinPoint.getSignature().toShortString());

        return result;
    }
}
