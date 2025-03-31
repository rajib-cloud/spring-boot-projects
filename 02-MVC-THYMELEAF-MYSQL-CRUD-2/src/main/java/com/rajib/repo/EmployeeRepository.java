package com.rajib.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajib.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
