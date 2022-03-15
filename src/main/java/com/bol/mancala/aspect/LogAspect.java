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


    /**
     * @param joinPoint   An interface which provides information about the joint point
     * @param returnValue the return value of the target method
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            , returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {

        // pointcut defines the join points(methods annotated by GetMapping and PostMapping) where
        // logging is done. the logging done before calling methods and after the methods return response

        LOGGER.info("invoking method {} with request {} and response {}"
                , joinPoint.getSignature().getName(), joinPoint.getArgs()
                , returnValue == null ? null : returnValue.toString());

    }


    /**
     * @param joinPoint An interface which provides information about the joint point
     * @param e         the exception thrown on join points
     */
    @AfterThrowing(value = "@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            , throwing = "e")
    public void endpointAfterThrowing(JoinPoint joinPoint, Exception e) {

        //when exceptions throw on joint points annotated with GetMapping and PostMapping,
        // the request and exception message is logged

        LOGGER.error("Exception: invoking method {} with input {} and response {}"
                , joinPoint.getSignature().getName(), joinPoint.getArgs()
                , e);
    }

}
