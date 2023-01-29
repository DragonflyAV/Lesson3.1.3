package ru.vasiliev.springcourse.FirstSecurityApp.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vasiliev.springcourse.FirstSecurityApp.security.ForUserDetails;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping()
    public String startPageUser(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ForUserDetails userDetails = (ForUserDetails) authentication.getPrincipal();
        model.addAttribute("user", userDetails.getUser());
        return "pages/user";
    }
}
