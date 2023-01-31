package ru.vasiliev.springcourse.FirstSecurityApp.Controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vasiliev.springcourse.FirstSecurityApp.models.Role;
import ru.vasiliev.springcourse.FirstSecurityApp.models.User;
import ru.vasiliev.springcourse.FirstSecurityApp.repositories.RoleRepository;
import ru.vasiliev.springcourse.FirstSecurityApp.services.UserService;
import ru.vasiliev.springcourse.FirstSecurityApp.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public AdminController(UserValidator userValidator, RoleRepository roleRepository, UserService userService) {
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping()
    public String startPageAdmin(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("user", user);
        return "pages/admin";
    }

    @GetMapping("/index")
    public String index(ModelMap model) {
        model.addAttribute("users", userService.findAll());
        return "pages/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("user", userService.findOne(id));
        return "pages/show";
    }

    @GetMapping("/new")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("/auth/new");
        mav.addObject("user", user);
        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/new")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/new";
        }
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUser(@PathVariable(name = "id") int id) {
        User user = userService.findOne(id);
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
        userService.update(id, user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin/index";
    }
}
