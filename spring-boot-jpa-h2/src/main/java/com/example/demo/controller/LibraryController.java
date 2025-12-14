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

import jakarta.validation.Valid;

@RestController
public class LibraryController {
	@Autowired
	private BookService bookService;

	@Autowired
	private BorrowerService borrowerService;

	/**
	 * Registers a new book in the library system.
	 * 
	 * This endpoint validates the book details and checks for ISBN conflicts. If a
	 * book with the same ISBN exists, it must have matching title and author.
	 * 
	 * @param book The book object to register (validated with @Valid)
	 * @return the registered book with generated ID
	 * 
	 *         Error handling: Validation errors return 400 Bad Request
	 *         automatically through @Valid annotation without using ResponseEntity
	 * @see Book
	 * @see BookService#registerBook(Book)
	 */
	@PostMapping("/book")
	public Book addBook(@Valid @RequestBody Book book) {
		return bookService.registerBook(book);
	}

	/**
	 * Retrieves all books in the library system.
	 * 
	 * This endpoint returns a complete list of all books regardless of their
	 * borrowing status. Each book includes borrower information if currently
	 * borrowed.
	 * 
	 * @return list of all books
	 * 
	 *         Returns empty list if no books found, rather than error code
	 * 
	 * @see BookService#getBooks()
	 */
	@GetMapping("/books")
	public List<Book> getAllBook() {
		return bookService.getBooks();
	}

	/**
	 * Registers a new borrower in the library system.
	 * 
	 * This endpoint creates a new borrower account with validated information. The
	 * borrower can then be associated with book borrowing transactions.
	 * 
	 * @param borrower The borrower object to register (validated with @Valid)
	 * 
	 * @return the registered borrower with generated ID Error handling: Validation
	 *         errors return 400 Bad Request automatically through @Valid annotation
	 * @see Borrower
	 * @see BorrowerService#registerBorrower(Borrower)
	 */
	@PostMapping("/borrower")
	public Borrower addBorrower(@Valid @RequestBody Borrower borrower) {
		return borrowerService.registerBorrower(borrower);
	}

	/**
	 * Processes a book borrowing transaction.
	 * 
	 * This endpoint assigns a book to a borrower if the book is available. It
	 * validates that both the book and borrower exist, and checks if the book is
	 * already borrowed.
	 * 
	 * Business rules: - Book must exist in the system - Book must not be currently
	 * borrowed - Borrower must be registered in the system
	 * 
	 * @param bookId     The ID of the book to borrow (path variable)
	 * @param borrowerId The ID of the borrower (query parameter)
	 * 
	 * @return the updated book with borrower information
	 * 
	 *         Error handling: Service layer throws exceptions for: - Book not found
	 *         (404) - Borrower not found (404) - Book already borrowed (409)
	 * 
	 *         Example: POST /api/library/books/1/borrow?borrowerId=5
	 * 
	 * @see BookService#borrowBook(int, int)
	 */
	@PostMapping("/borrowBook")
	public Book borrowBook(@RequestParam int bookId, @RequestParam int borrowerId) {

		return bookService.borrowBook(bookId, borrowerId);
	}

	/**
	 * Processes a book return transaction.
	 * 
	 * This endpoint marks a borrowed book as returned and removes the borrower
	 * association. It validates that the book exists and is currently borrowed
	 * before processing the return.
	 * 
	 * Business rules: - Book must exist in the system - Book must be currently
	 * borrowed (cannot return an available book)
	 * 
	 * @param bookId The ID of the book to return (path variable)
	 * @return the updated book with borrower information cleared Error handling:
	 *         Service layer throws exceptions for: - Book not found (404) - Book
	 *         not currently borrowed (409) Example: POST
	 *         /api/library/books/1/return
	 * 
	 * @see BookService#returnBook(int)
	 */
	@PostMapping("/returnBook")
	public Book returnBook(@RequestParam int bookId) {
		return bookService.returnBook(bookId);
	}

}
