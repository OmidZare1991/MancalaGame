package com.bol.mancala.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class LogAspect {

    @AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.GetMapping)",
            returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        Object[] signatureArgs = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        System.out.println("signature: " + signature.getName()); // method name

        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        for(int i =0;i<parameterAnnotations.length;i++){
        }

    }
}
