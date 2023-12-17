package com.github.bondarevv23.task_management_system.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class InboundRequestAdvice {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restRequest() { }

    @Before("restRequest()")
    public void beforeRequest(JoinPoint jp) {
        log.info(String.format("Accepted request: %s", getRestRequestString(jp)));
    }

    @After("restRequest()")
    public void afterRequest(JoinPoint jp) {
        log.info(String.format("Sent response: %s", getRestRequestString(jp)));
    }

    private String getRestRequestString(JoinPoint jp) {
        return String.format(
                "%s with arguments: %s",
                jp.toShortString(),
                Arrays.stream(jp.getArgs())
                        .map(Object::toString)
                        .collect(Collectors.joining(","))
                );
    }
}
