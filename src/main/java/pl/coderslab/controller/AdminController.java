package pl.coderslab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/start")
    public String start(Model model) {
        return "/admin/start.jsp";
    }

    @GetMapping("/users")
    public String usersAll(Model model) {
        model.addAttribute("users", userRepository.findUserByRole("USER"));
        return "/user/userAll.jsp";
    }
}
