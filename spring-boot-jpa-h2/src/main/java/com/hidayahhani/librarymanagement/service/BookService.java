package com.hidayahhani.librarymanagement.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hidayahhani.librarymanagement.entity.Book;
import com.hidayahhani.librarymanagement.entity.Borrower;
import com.hidayahhani.librarymanagement.repo.BookRepository;
import com.hidayahhani.librarymanagement.repo.BorrowerRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BorrowerRepository borrowerRepository;

	public Book registerBook(Book book) {
		List<Book> existingBooks = bookRepository.findByIsbn(book.getIsbn());

		if (!existingBooks.isEmpty()) {
			Book existing = existingBooks.get(0);

			if (!existing.getTitle().equalsIgnoreCase(book.getTitle()) || !existing.getAuthor().equalsIgnoreCase(book.getAuthor())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"Books with the same ISBN must have the same title and author");
			}
		}
		return bookRepository.save(book);
	}

	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	public Book borrowBook(int bookId, int borrowerId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		
		if (Objects.isNull(book)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
		}

		if (book.isBorrowed()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is already borrowed");
		}

		Borrower borrower = borrowerRepository.findById(borrowerId).orElse(null);

		if (Objects.isNull(borrower)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower does not exist");
		}

		book.setBorrowed(true);
		book.setBorrower(borrower);

		return bookRepository.save(book);
	}

	public Book returnBook(int bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);

		if (Objects.isNull(book)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
		}

		if (!book.isBorrowed()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is not borrowed");
		}

		book.setBorrowed(false);
		book.setBorrower(null);

		return bookRepository.save(book);
	}

}
