package zerobase.weather.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Around("@within(zerobase.weather.aop.Logging) || @annotation(zerobase.weather.aop.Logging)")
    public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            log.info("ThreadID = {} : {} 호출", Thread.currentThread().getId(), joinPoint.getSignature());

            Object result = joinPoint.proceed();

            log.info("ThreadID = {} : {} 정상 종료", Thread.currentThread().getId(), joinPoint.getSignature());
            return result;
        } catch (Exception e)
        {
            log.error("ThreadID = {} : {} 오류 발생", Thread.currentThread().getId(), joinPoint.getSignature());
            throw e;
        }
    }
}
