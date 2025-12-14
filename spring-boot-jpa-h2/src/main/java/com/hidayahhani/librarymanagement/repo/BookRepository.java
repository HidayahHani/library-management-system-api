package com.hidayahhani.librarymanagement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hidayahhani.librarymanagement.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

	List<Book> findByIsbn(String isbn);

}
