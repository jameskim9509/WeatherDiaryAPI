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
    public void doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("{} 호출", joinPoint.getSignature());
        try{
            joinPoint.proceed();
        } catch (Exception e)
        {
            log.error("{} 오류 발생", joinPoint.getSignature());
            throw e;
        }
        log.info("{} 정상 종료", joinPoint.getSignature());
    }
}
