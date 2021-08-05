package pl.coderslab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.entity.Company;
import pl.coderslab.repository.CompanyRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyRepository companyRepository;


    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @GetMapping("/add")
    public String companyAdd(Model model) {
        model.addAttribute("company", new Company());
        return "/company/companyAddForm.jsp";
    }

    @PostMapping("/add")
    public String companyAddPost(@Valid Company company, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/company/companyAddForm.jsp";
        }
        companyRepository.save(company);
        model.addAttribute("companys", companyRepository.findAll());
        return "/company/companyAll.jsp";

    }

    @GetMapping("all")
    public String companyAll(Model model) {
        model.addAttribute("companys", companyRepository.findAll());
        return "/company/companyAll.jsp";
    }
}
