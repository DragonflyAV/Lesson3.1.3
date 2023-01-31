package ru.vasiliev.springcourse.FirstSecurityApp.util;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vasiliev.springcourse.FirstSecurityApp.models.User;
import ru.vasiliev.springcourse.FirstSecurityApp.services.UserServiceImpl;

@Component
public class UserValidator implements Validator {

    private final UserServiceImpl userServiceImpl;

    public UserValidator(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userServiceImpl.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("username", "", "Человек с таким именем уже существует");
    }
}
