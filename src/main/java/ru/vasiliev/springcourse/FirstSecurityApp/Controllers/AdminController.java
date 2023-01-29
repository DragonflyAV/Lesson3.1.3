package ru.vasiliev.springcourse.FirstSecurityApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vasiliev.springcourse.FirstSecurityApp.models.Role;
import ru.vasiliev.springcourse.FirstSecurityApp.models.User;
import ru.vasiliev.springcourse.FirstSecurityApp.repositories.RoleRepository;
import ru.vasiliev.springcourse.FirstSecurityApp.security.ForUserDetails;
import ru.vasiliev.springcourse.FirstSecurityApp.services.AdminService;
import ru.vasiliev.springcourse.FirstSecurityApp.services.RegistrationService;
import ru.vasiliev.springcourse.FirstSecurityApp.services.UserService;
import ru.vasiliev.springcourse.FirstSecurityApp.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    private final RegistrationService registrationService;

    @Autowired
    public AdminController(AdminService adminService, UserValidator userValidator, RoleRepository roleRepository, RegistrationService registrationService) {
        this.adminService = adminService;
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
        this.registrationService = registrationService;
    }

    @GetMapping()
    public String startPageAdmin(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ForUserDetails userDetails = (ForUserDetails) authentication.getPrincipal();
        model.addAttribute("user", userDetails.getUser());
        return "pages/admin";
    }

    @GetMapping("/index")
    public String index(ModelMap model) {
        model.addAttribute("users", adminService.findAll());
        return "pages/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("user", adminService.findOne(id));
        return "pages/show";
    }

    @GetMapping("/registration")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("auth/registration");
        mav.addObject("user", user);
        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        registrationService.register(user);
        return "redirect:/admin/";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUser(@PathVariable(name = "id") int id) {
        User user = adminService.findOne(id);
        ModelAndView mav = new ModelAndView("pages/edit");
        mav.addObject("user", user);
        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "pages/edit";
        }
        adminService.update(id, user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        adminService.delete(id);
        return "redirect:/admin/";
    }
}
