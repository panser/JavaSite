package ua.org.gostroy.model;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ua.org.gostroy.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Locale;
import java.util.Set;

/**
 * Created by panser on 6/6/2014.
 */
public class ValidatorTests {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

//    @Test
    public void emptyLogin() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.setLogin("");
        user.setEmail("JUnit@gostroy.org.ua");
        user.setPassword("junit");

        Validator validator = createValidator();
        log.trace("emptyLogin(), validator: " + validator);
        log.trace("emptyLogin(), user: " + user);
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        for(ConstraintViolation<User> violation : constraintViolations){
            log.trace("emptyLogin(), violation: " + violation);
        }

        Assert.assertEquals(1, constraintViolations.size());
        ConstraintViolation<User> violation =  constraintViolations.iterator().next();
        Assert.assertEquals(violation.getPropertyPath().toString(), "login");
        Assert.assertEquals(violation.getMessage(), "Login is required");
    }

}

