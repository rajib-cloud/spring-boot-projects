package com.rajib.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajib.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
