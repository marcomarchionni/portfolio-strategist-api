package com.marcomarchionni.ibportfolio.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.marcomarchionni.ibportfolio.logging.LoggingUtils.*;

@Aspect
@Component
public class ValidatorLoggingAspect {

    @Pointcut("execution(boolean com.marcomarchionni.ibportfolio.model.validation.*.isValid(..))")
    public void validatorIsValidMethods() {}

    @AfterReturning(pointcut="validatorIsValidMethods() && args(value,..)", returning="isValid", argNames = "joinPoint,value,isValid")
    public void logParamIsValid(JoinPoint joinPoint, Object value, boolean isValid){
        String className = getSimpleClassName(joinPoint);
        if (isValid) {
            logOk( className + " validates " + value);
        } else {
            logWarning(className + " DOES NOT VALIDATE " + value);
        }
    }
}
