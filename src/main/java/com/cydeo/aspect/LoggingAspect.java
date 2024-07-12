package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

//    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount userDetails = (SimpleKeycloakAccount) authentication.getDetails();
        return userDetails.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    @Pointcut("execution(* com.cydeo.controller.ProjectController.*(..)) || " +
            "execution(* com.cydeo.controller.TaskController.*(..))")
    public void anyProjectAndTaskControllerPointCut() {
    }

    @Before("anyProjectAndTaskControllerPointCut()")
    public void beforeAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint) {

        log.info("Before -> Method: {}, User: {}",
                joinPoint.getSignature().toShortString(),
                getUserName());
    }

    @AfterReturning(pointcut = "anyProjectAndTaskControllerPointCut()", returning = "results")
    public void afterAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint, Object results) {

        log.info("After Returning -> Method: {}, User: {}, Results: {}",
                joinPoint.getSignature().toShortString(),
                getUserName(),
                results.toString());
    }

    @AfterThrowing(pointcut = "anyProjectAndTaskControllerPointCut()", throwing = "exception")
    public void afterReturningAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint, Exception exception) {

        log.info("After Throwing -> Method: {}, User: {}, Results: {}",
                joinPoint.getSignature().toShortString(),
                getUserName(),
                exception.getMessage());
    }
}
