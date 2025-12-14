package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Book;
import com.example.demo.entity.Borrower;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowerService;

import jakarta.validation.Valid;

@RestController
public class LibraryController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BorrowerService borrowerService;
	
	@PostMapping("/addBook")
	public Book addBook(@Valid @RequestBody Book book) {
		return bookService.registerBook(book);
	}

	@GetMapping("/books")
	public List<Book> getAllBook() {
		return bookService.getBooks();
	}
	
	@PostMapping("/addBorrower")
	public Borrower addBorrower(@Valid @RequestBody Borrower borrower) {
		return borrowerService.registerBorrower(borrower);
	}
	
	@PostMapping("/borrowBook")
	public ResponseEntity<?> borrowBook(
            @RequestParam int bookId,
            @RequestParam int borrowerId) {

        try {
            Book book = bookService.borrowBook(bookId, borrowerId);
            return ResponseEntity.ok(book); // 200

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
        }
    }

	
	@PostMapping("/returnBook")
//	public Book returnBook(@RequestParam int bookId) {
//		return bookService.returnBook(bookId);
//	}
	public ResponseEntity<?> returnBook(@RequestParam int bookId) {

        try {
            Book book = bookService.returnBook(bookId);
            return ResponseEntity.ok(book); // 200

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
        }
    }


}
