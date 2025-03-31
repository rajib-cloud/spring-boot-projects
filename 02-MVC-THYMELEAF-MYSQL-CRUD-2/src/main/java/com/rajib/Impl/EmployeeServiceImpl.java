package com.rajib.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajib.entity.Employee;
import com.rajib.exception.EmployeeNotFoundException;
import com.rajib.repo.EmployeeRepository;
import com.rajib.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepo;

	@Override
	public Integer saveEmployee(Employee e) {
		e = employeeRepo.save(e);
		return e.getEmpId();
	}

	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> list = employeeRepo.findAll();
		return list;
	}

	@Override
	public void deleteEmployee(Integer id) {
		employeeRepo.deleteById(id);
		
	}

	@Override
	public Employee getOneEmployee(Integer id) {
		Employee e = employeeRepo.findById(id).orElseThrow(
				()-> new EmployeeNotFoundException("Employee not found on id :"+id)
				);
		return e;
	}

	@Override
	public void updateEmployee(Employee e) {
		employeeRepo.findById(e.getEmpId()).orElseThrow(
				()-> new EmployeeNotFoundException("Employee'"+e.getEmpId()+"' Not found ")
				);
		employeeRepo.save(e);
		
	}
	
}
