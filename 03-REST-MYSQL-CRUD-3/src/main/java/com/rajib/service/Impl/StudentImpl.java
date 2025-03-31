package com.rajib.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.rajib.entity.Student;
import com.rajib.exception.StudentNotFoundException;
import com.rajib.repo.StudentRepository;
import com.rajib.service.StudentService;

public class StudentImpl implements StudentService{

	@Autowired
	private StudentRepository repo;
	
	@Override
	public Integer saveStudent(Student student) {
		Student s = repo.save(student);
		return s.getStdId();
	}

	@Override
	public void updateStudent(Student student) {
		if(student.getStdId() == null || !repo.existsById(student.getStdId())) {
			throw new StudentNotFoundException("Student '"+student.getStdId()+"' not found.");
		}else {
			repo.save(student);
		}
		
	}

	@Override
	public void deleteStudent(Integer id) {
		repo.delete(getOneStudent(id));
		
	}

	@Override
	public Student getOneStudent(Integer id) {
		
		return repo.findById(id).orElseThrow(
				()-> new StudentNotFoundException("Student '"+id+"' not found.")
				);
	}

	@Override
	public List<Student> getAllStudents() {
		
		return repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

}
