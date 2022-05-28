package app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

//    @Around("@annotation(app.annotation.ToLog)")
//    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
//        logger.info("Method will execute");
//        joinPoint.proceed();
//        logger.info("Method executed");
//    }


    @AfterReturning(value = "@annotation(app.annotation.ToLog)", returning = "returnedValue")
    public void log(Object returnedValue) throws Throwable {
        logger.info("--- @AfterReturning is used");
        logger.info("Method executed and returned: "  + returnedValue);
        logger.info("--- Aspect done");
    }
}
