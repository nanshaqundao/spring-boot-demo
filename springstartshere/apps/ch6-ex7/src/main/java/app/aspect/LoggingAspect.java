package app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
@Order(3)
public class LoggingAspect {
    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("@annotation(app.annotation.ToLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Logging Aspect - Order 3: Calling the intercepted method");
        Object returnedValue = joinPoint.proceed();
        logger.info("Logging Aspect - Order 3: Method executed and returned: " + returnedValue);
        return returnedValue;
    }


//    @AfterReturning(value = "@annotation(app.annotation.ToLog)", returning = "returnedValue")
//    public void log(Object returnedValue) throws Throwable {
//        logger.info("--- @AfterReturning is used");
//        logger.info("Method executed and returned: "  + returnedValue);
//        logger.info("--- Aspect done");
//    }


}
