package pl.coderslab.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.entity.Addresses;
import pl.coderslab.entity.FaultOrder;
import pl.coderslab.entity.User;
import pl.coderslab.repository.AddressesRepository;
import pl.coderslab.repository.FaultOrderRepository;
import pl.coderslab.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/faultOrder")
public class FaultOrderController {
    private final FaultOrderRepository faultOrderRepository;
    private final AddressesRepository addressesRepository;
    private final UserRepository userRepository;

    public FaultOrderController(FaultOrderRepository faultOrderRepository, AddressesRepository addressesRepository, UserRepository userRepository) {
        this.faultOrderRepository = faultOrderRepository;
        this.addressesRepository = addressesRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/add")
    public String faultOrderAdd(Model model, HttpServletRequest request) {
        if (request.getParameter("id").isEmpty()) {
            model.addAttribute("faultOrder", new FaultOrder());
        } else {
            String id = request.getParameter("id");
            model.addAttribute("faultOrder", faultOrderRepository.findById(Long.parseLong(id)));
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("addresses", addressesRepository.findAddressesByUser((User) principal));
        return "/faultOrder/faultOrderAddForm.jsp";
    }

    @PostMapping("/add")
    public String faultOrderAddPost(@Valid FaultOrder faultOrder, BindingResult result, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors()) {
            model.addAttribute("addresses", addressesRepository.findAddressesByUser((User) principal));
            return "/faultOrder/faultOrderAddForm.jsp";
        }
        faultOrder.setClient((User) principal);
        faultOrderRepository.save(faultOrder);
        return "redirect:/user/start";
    }

    @GetMapping("/all")
    public String faultOrderAll(Model model) {
        model.addAttribute("faultOrder", faultOrderRepository.findAll());
        return "/faultOrder/faultOrderAll.jsp";
    }

    @GetMapping("/update")
    public String faultOrderUpdate(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        model.addAttribute("faultOrder", faultOrderRepository.findById(Long.parseLong(id)).get());
        model.addAttribute("users", userRepository.findUserByRole("USER"));
        return "/faultOrder/faultOrderUpdate.jsp";
    }

    @PostMapping("/update")
    public String faultOrderUpdatePost(@Valid FaultOrder faultOrder,BindingResult result, Model model,HttpServletRequest request) {
      if(result.hasErrors()){
          String id = request.getParameter("id");
          model.addAttribute("faultOrder", faultOrderRepository.findById(Long.parseLong(id)).get());
          model.addAttribute("users", userRepository.findUserByRole("USER"));
          return "/faultOrder/faultOrderUpdate.jsp";
      }
        FaultOrder faultOrder1 = faultOrderRepository.findById(faultOrder.getId()).get();
        faultOrder1.setStatus(faultOrder.getStatus());
        faultOrder1.setUser(faultOrder.getUser());
        faultOrderRepository.save(faultOrder1);

        return "redirect:/admin/start";
    }
}