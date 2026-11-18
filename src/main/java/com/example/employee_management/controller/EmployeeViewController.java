package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.service.DepartmentService;
import com.example.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeViewController(EmployeeService employeeService,
            DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            Model model) {
        model.addAttribute("employees", employeeService.search(name, department));
        model.addAttribute("name", name);
        model.addAttribute("department", department);
        return "employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeRequest", new EmployeeRequest());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("employeeRequest") EmployeeRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            try {
                employeeService.create(request);
                redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thành công");
                return "redirect:/employees/list";
            } catch (DuplicateResourceException ex) {
                result.rejectValue("email", "duplicate", ex.getMessage());
            }
        }
        model.addAttribute("departments", departmentService.findAll());
        return "employees/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EmployeeResponse employee = employeeService.findById(id);
        EmployeeRequest request = new EmployeeRequest();
        request.setName(employee.getName());
        request.setEmail(employee.getEmail());
        request.setDepartmentId(employee.getDepartmentId());
        model.addAttribute("employeeRequest", request);
        model.addAttribute("employeeId", id);
        model.addAttribute("employeeCode", employee.getCode());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
            @Valid @ModelAttribute("employeeRequest") EmployeeRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            try {
                employeeService.update(id, request);
                redirectAttributes.addFlashAttribute("message", "Cập nhật nhân viên thành công");
                return "redirect:/employees/list";
            } catch (DuplicateResourceException ex) {
                result.rejectValue("email", "duplicate", ex.getMessage());
            }
        }
        model.addAttribute("employeeId", id);
        model.addAttribute("departments", departmentService.findAll());
        return "employees/edit";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Đã xóa nhân viên");
        return "redirect:/employees/list";
    }
}