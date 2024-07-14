package org.example;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class AspectTest {
    @Around("execution(* org.example.service.*.*(..))")
    public Object measureServiceMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();
        log.info(joinPoint.getSignature().toShortString() + " executed in " + stopWatch.getTotalTimeMillis() + " ms");
        return result;
    }

}
