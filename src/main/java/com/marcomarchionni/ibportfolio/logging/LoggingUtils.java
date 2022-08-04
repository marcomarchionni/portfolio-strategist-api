package com.marcomarchionni.ibportfolio.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

import java.util.List;

@Slf4j
public class LoggingUtils {

    public static String getClassAndMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().toShortString();
    }

    public static String getSimpleClassName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringType().getSimpleName();
    }

    public static String getEntitiesNumberAndName(List<?> resultEntities) {

        if (resultEntities.size() == 0) {
            return "0 entities";
        } else {
            int entityNumber = resultEntities.size();
            String entityName = resultEntities.get(0).getClass().getSimpleName();
            return entityNumber + " " + entityName + "(s)";
        }
    }

    public static String getParamNamesAndValues(JoinPoint joinPoint) {
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        StringBuilder paramNamesAndValues = new StringBuilder();
        for ( int i = 0; i < paramNames.length; i++) {
            paramNamesAndValues.append(paramNames[i]).append(": ").append(paramValues[i]).append(" ");
        }
        return paramNamesAndValues.toString();
    }

    public static void logCall(String string) {
        log.info(">>>> {}", string);
    }
    public static void logPlain(String string) {
        log.info("---- {}", string);
    }
    public static void logReturn(String string) {
        log.info("<<<< {}", string);
    }
    public static void logWarning(String string) {
        log.warn("!!!! {}", string);
    }
    public static void logOk(String string) {
        log.info("++++ {}", string);
    }
}

