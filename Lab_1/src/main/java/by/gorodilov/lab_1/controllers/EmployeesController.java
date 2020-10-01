package by.gorodilov.lab_1.controllers;

import by.gorodilov.lab_1.forms.EmployeeForm;
import by.gorodilov.lab_1.models.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class EmployeesController {
    private static List<Employee> employees = new ArrayList<>();

    static {
        employees.add(new Employee("George", ".net"));
        employees.add(new Employee("Michel", "js"));
    }

    // Вводится (inject) из application.properties.
    @Value("${welcome.message}")
    private String message;
    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message", message);

        log.info("/index was called");

        return modelAndView;
    }

    @GetMapping(value = "/allEmployees")
    public ModelAndView employeesList(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employees");

        model.addAttribute("employees", employees);
        return modelAndView;
    }

    @RequestMapping(value = {"/addemployee"}, method = RequestMethod.GET)
    public ModelAndView showAddPersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("addemployee");
        EmployeeForm employeeForm = new EmployeeForm();
        model.addAttribute("employeeform", employeeForm);
        return modelAndView;
    }

    // @PostMapping("/addemployee")
//GetMapping("/")
    @RequestMapping(value = {"/addemployee"}, method = RequestMethod.POST)
    public ModelAndView savePerson(Model model, //
                                   @ModelAttribute("employeeform") EmployeeForm employeeForm) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employees");
        String name = employeeForm.getName();
        String specialization = employeeForm.getSpecialization();
        if (name != null && name.length() > 0 //
                && specialization != null && specialization.length() > 0) {
            Employee newEmployee = new Employee(name, specialization);
            employees.add(newEmployee);
            model.addAttribute("employees", employees);
            return modelAndView;
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addemployee");
        return modelAndView;
    }

    @RequestMapping(value = {"/editemployee"}, method = RequestMethod.POST)
    public ModelAndView editPerson(Model model, //
                                   @ModelAttribute("employeeform") EmployeeForm employeeForm) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employees");
        String name = employeeForm.getName();
        String specialization = employeeForm.getSpecialization();
        if (name != null && name.length() > 0 //
                && specialization != null && specialization.length() > 0) {
            Employee withSameName = employees.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
            if (withSameName != null) {
                withSameName.setSpecialization(specialization);

                model.addAttribute("employees", employees);
                return modelAndView;
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addemployee");
        return modelAndView;
    }

    @RequestMapping(value = {"/deleteemployee"}, method = RequestMethod.POST)
    public ModelAndView deletePerson(Model model, //
                                     @ModelAttribute("employeeform") EmployeeForm employeeForm) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employees");
        String name = employeeForm.getName();
        if (name != null && name.length() > 0) {
            boolean deleted = employees.removeIf(e -> e.getName().equals(name));
            if (deleted) {
                model.addAttribute("employees", employees);
                return modelAndView;
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addemployee");
        return modelAndView;
    }
}
