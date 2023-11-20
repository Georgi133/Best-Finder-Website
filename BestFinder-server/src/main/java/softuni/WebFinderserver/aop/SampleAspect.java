package softuni.WebFinderserver.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SampleAspect {

    @Pointcut("execution(* softuni.WebFinderserver.init.AdminInit.run(..))")
    void methodInitAdmin(){}

    @Before("methodInitAdmin()")
    public void beforeAdminInit() {
        log.info("Starting to persist admin in the base");
    }

    @AfterThrowing("methodInitAdmin()")
    public void ifNotFilledDataInYaml() {
        log.info("ADMIN DATA IN YAML NOT FILLED !!");
    }

    @AfterReturning("methodInitAdmin()")
    public void successfullySavedAdmin() {
        log.info("ADMIN saved to the base !!!");
    }

}
