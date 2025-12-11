package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Book;
import com.example.demo.entity.Borrower;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowerService;

@RestController
public class LibraryController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BorrowerService borrowerService;
	
	@PostMapping("/addBook")
	public Book addBook(@RequestBody Book book) {
		return bookService.registerBook(book);
	}
	
	@GetMapping("/books")
	public List<Book> getAllBook() {
		return bookService.getBooks();
	}
	
	@PostMapping("/addBorrower")
	public Borrower addBorrower(@RequestBody Borrower borrower) {
		return borrowerService.registerBorrower(borrower);
	}
	
	@PostMapping("/borrowBook")
	public Book borrowBook(@RequestParam int bookId, @RequestParam int borrowerId) {
		return bookService.borrowBook(bookId, borrowerId);
	}
	
	@PostMapping("/returnBook")
	public Book returnBook(@RequestParam int bookId) {
		return bookService.returnBook(bookId);
	}

}
