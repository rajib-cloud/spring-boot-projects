package com.rajib.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajib.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
