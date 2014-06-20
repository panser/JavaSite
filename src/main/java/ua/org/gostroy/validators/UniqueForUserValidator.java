package ua.org.gostroy.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.model.User;
import ua.org.gostroy.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by panser on 6/20/2014.
 */
public class UniqueForUserValidator implements ConstraintValidator<UniqueForUser, User> {
    private String message = "";
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired(required = true)
    private MessageSource messageSource;
    @Autowired
    UserService userService;

    @Override
    public void initialize(UniqueForUser constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isValid(User user, ConstraintValidatorContext context) {
        Object checkByLogin = userService.findByLogin(user.getLogin());
        if(checkByLogin != null){
            message = messageSource.getMessage("validation.user.login.Duplicate", null, LocaleContextHolder.getLocale());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addNode("login").addConstraintViolation();
            return false;
        }

        return true;
    }
}
