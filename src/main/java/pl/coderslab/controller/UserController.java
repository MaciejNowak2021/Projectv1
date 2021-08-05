package pl.coderslab.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.entity.Addresses;
import pl.coderslab.entity.User;
import pl.coderslab.repository.AddressesRepository;
import pl.coderslab.repository.CompanyRepository;
import pl.coderslab.repository.FaultOrderRepository;
import pl.coderslab.repository.UserRepository;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final FaultOrderRepository faultOrderRepository;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository, FaultOrderRepository faultOrderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.faultOrderRepository = faultOrderRepository;
    }

    @ModelAttribute
    public void addAttribute(Model model) {
        model.addAttribute("companys", companyRepository.findAll());
    }


    @GetMapping("/add")
    public String clientAdd(Model model) {
        model.addAttribute("user", new User());
        return "/user/clientAddForm.jsp";
    }

    @PostMapping("/add")
    public String clientAddPost(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "/user/clientAddForm.jsp";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/login";

    }

    @GetMapping("/all")
    public String userAll(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/userAll.jsp";
    }

    @GetMapping("/start")
    public String start(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("user", principal);
        model.addAttribute("order", faultOrderRepository.findFaultOrdersByClient((User) principal));
        return "/user/start.jsp";
    }

    @GetMapping("/add/user")
    public String userAdd(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("company", companyRepository.findAll());
        return "/user/userAddForm.jsp";

    }

    @PostMapping("/add/user")
    public String userAddPost(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("company", companyRepository.findAll());
            return "/user/userAddForm.jsp";
        }
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
            model.addAttribute("company", companyRepository.findAll());
            model.addAttribute("error","Użytkownik o podanym adresie email istnieje!");
            return "/user/userAddForm.jsp";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/admin/users";

    }

}