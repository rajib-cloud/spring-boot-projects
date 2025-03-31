package com.rajib.runner;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rajib.entity.Book;
import com.rajib.exception.BookNotFoundException;
import com.rajib.repo.BookRepository;

@Component
public class BookRunner implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepo;
	
	
	@Override
	public void run(String... args) throws Exception {
		
		Book b1 = new Book(10, "Core Java", "SAM" , 200.0);
		bookRepo.save(b1);
		
		bookRepo.save(new Book(11, "ADv java", "stalin", 300.0));
		bookRepo.save(new Book(12, "Spring","Arvind", 400.0));//insert
		bookRepo.save(new Book(12, "Spring 6.x","Arvind", 500.0));//update
		
		bookRepo.saveAll(Arrays.asList(
				new Book(13, "HTML CSS", "AJAYA", 100.0),
				new Book(14,"BOOTSTRAP", "RAJ",900.0),
				new Book(15, "Reactjs", "Kapil", 700.0)
				));
		
		bookRepo.findAll().forEach(System.out::println);
		
		System.out.println(bookRepo.count());
		
		System.out.println(bookRepo.existsById(13));
		
		Book b4 = bookRepo.findById(14).orElseThrow(
				() ->  new BookNotFoundException("Book not found in this id .")
				);
		System.out.println(b4);
		
		

	}

}
