package com.rajib.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rajib.entity.Employee;
import com.rajib.exception.EmployeeNotFoundException;
import com.rajib.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/register")
	public String showRegister() {
		return "EmployeeRegister";
	}
	
	@PostMapping("/save")
	public String saveForm(@ModelAttribute Employee employee, Model model) {
		Integer id = employeeService.saveEmployee(employee);
		
		String message = "Employee '"+id+"' created";
		model.addAttribute("message",message);
		
		return "EmployeeRegister";
		
	}
	
	@GetMapping("/all")
	public String getAllEmployee(@RequestParam(required = false)String message, Model model) {
		List<Employee> list = employeeService.getAllEmployees();
		model.addAttribute("list",list);
		model.addAttribute("message", message);
		return "EmployeeData";
	}
	
	@GetMapping("/delete")
	public String deleteEmployee(@RequestParam Integer id, RedirectAttributes attributes) {
		try {
			employeeService.deleteEmployee(id);
			attributes.addAttribute("message", "Employee '"+id+"' Deleted.");
		}catch(EmployeeNotFoundException e) {
			e.printStackTrace();
			attributes.addAttribute("message",e.getMessage());
		}
		return "redirect:all";
	}
	@GetMapping("/edit")
	public String showEdit(@RequestParam Integer id, Model model) {
		String page = null;
		try {
			Employee employee = employeeService.getOneEmployee(id);
			model.addAttribute("employee", employee);
			page = "EmployeeEdit";
		}catch(EmployeeNotFoundException e) {
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			page = "redirect:all";
		}
		return page;
	}
}
