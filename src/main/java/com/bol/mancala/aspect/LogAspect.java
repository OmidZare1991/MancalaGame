package com.bol.mancala.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Using Aspect to log the requests and responses when endpoints in the controller called.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);


    // pointcut defines the join points(methods annotated by GetMapping and PostMapping) where
    // logging is done. the logging done before calling methods and after the methods return response.

    @AfterReturning(pointcut = "@annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            , returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {

        Object[] args = joinPoint.getArgs();
        Object requestValue;

        if (args != null && args.length > 0) {
            requestValue = args[0];
        } else {
            requestValue = null;
        }

        LOGGER.info("invoking method {} with request {} and response {}"
                , joinPoint.getSignature().getName(), requestValue
                , returnValue == null ? null : returnValue.toString());

    }

    //when exceptions throw on joint points annotated with GetMapping and PostMapping,
    // the request and exception message is logged

    @AfterThrowing(value = "@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            , throwing = "e")
    public void endpointAfterThrowing(JoinPoint joinPoint, Exception e) {
        Object[] args = joinPoint.getArgs();
        LOGGER.error("Exception: invoking method {} with input {} and response {}"
                , joinPoint.getSignature().getName(), args.length == 0 ? null : args[0]
                , e == null ? null : e.toString());
    }

}
