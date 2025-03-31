package com.rajib.service;

import java.util.List;

import com.rajib.entity.Student;

public interface StudentService {

	Integer saveStudent(Student student);
	void updateStudent(Student student);
	void deleteStudent(Integer id);
	Student getOneStudent(Integer id);
	List<Student> getAllStudents();
}
