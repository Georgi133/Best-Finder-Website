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


    @AfterThrowing("methodInitAdmin()")
    public void ifNotFilledDataInYaml() {
        log.info("ADMIN DATA IN YAML NOT FILLED !!");
    }

    @AfterReturning("methodInitAdmin()")
    public void successfullySavedAdmin() {
        log.info("Head ADMIN is in the base !!!");
    }

}
