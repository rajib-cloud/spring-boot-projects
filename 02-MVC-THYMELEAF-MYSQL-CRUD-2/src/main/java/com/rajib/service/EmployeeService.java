package com.rajib.service;

import java.util.List;

import com.rajib.entity.Employee;

public interface EmployeeService {

	Integer saveEmployee(Employee e);
	List<Employee> getAllEmployees();
	void deleteEmployee(Integer id);
	Employee getOneEmployee(Integer id);
	void updateEmployee(Employee e);
}
