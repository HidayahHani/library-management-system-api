package com.hidayahhani.librarymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.hidayahhani.librarymanagement.entity.Book;
import com.hidayahhani.librarymanagement.entity.Borrower;
import com.hidayahhani.librarymanagement.repo.BookRepository;
import com.hidayahhani.librarymanagement.repo.BorrowerRepository;
import com.hidayahhani.librarymanagement.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	@Mock
	private BorrowerRepository borrowerRepository;

	@Test
	void testRegisterBookSuccess() {
		Book book = new Book();
		book.setIsbn("978-0-2154-9807-4");
		book.setAuthor("John Green");
		book.setTitle("Paper Towns");

		when(bookRepository.save(book)).thenReturn(book);

		Book savedBook = bookService.registerBook(book);

		assertNotNull(savedBook);
		assertEquals("Paper Towns", savedBook.getTitle());
		assertEquals("John Green", savedBook.getAuthor());
		assertEquals("978-0-2154-9807-4", savedBook.getIsbn());
	}

	@Test
	void testRegisterBookFailed() {
		Book existingBook = new Book();
		existingBook.setIsbn("01-321-7721");
		existingBook.setAuthor("Emily");
		existingBook.setTitle("Basic Programming");

		Book newBook = new Book();
		newBook.setIsbn("01-321-7721");
		newBook.setAuthor("Emily");
		newBook.setTitle("Advanced Programming");

		when(bookRepository.findByIsbn("01-321-7721")).thenReturn(Collections.singletonList(existingBook));

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			bookService.registerBook(newBook);
		});
		assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
		assertEquals("Books with the same ISBN must have the same title and author", exception.getReason());
	}

	@Test
	void testGetAllBooks() {
		Book book1 = new Book();
		book1.setIsbn("978-0-2154-9807-4");
		book1.setAuthor("John Green");
		book1.setTitle("Paper Towns");

		Book book2 = new Book();
		book2.setIsbn("9819-732-5252");
		book2.setAuthor("Emily Carter");
		book2.setTitle("Beyond The Horizon");

		List<Book> books = new ArrayList<>();
		books.add(book1);
		books.add(book2);

		when(bookRepository.findAll()).thenReturn(books);

		List<Book> result = bookService.getBooks();

		assertNotNull(books);
		assertEquals(2, result.size());
		assertEquals("Paper Towns", result.get(0).getTitle());
		assertEquals("John Green", result.get(0).getAuthor());
		assertEquals("Beyond The Horizon", result.get(1).getTitle());
		assertEquals("Emily Carter", result.get(1).getAuthor());
	}

	@Test
	void testBorrowBookSuccess() {
		Book book = new Book();
		book.setId(1);
		book.setBorrowed(false);

		Borrower borrower = new Borrower();
		borrower.setId(3);
		borrower.setName("Jenny");
		borrower.setEmail("jenny@gmail.com");

		when(bookRepository.findById(1)).thenReturn(Optional.of(book));
		when(borrowerRepository.findById(3)).thenReturn(Optional.of(borrower));
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		Book result = bookService.borrowBook(1, 3);

		assertNotNull(result);
		assertTrue(result.isBorrowed());
		assertEquals(borrower, result.getBorrower());
	}

	@Test
	void testBookNotFound() {
		when(bookRepository.findById(1)).thenReturn(Optional.empty());

		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
			bookService.borrowBook(1, 3);
		});

		assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
		assertEquals("Book not found", ex.getReason());

	}

	@Test
	void testBookAlreadyBorrowed() {
		Book book = new Book();
		book.setBorrowed(true);

		when(bookRepository.findById(1)).thenReturn(Optional.of(book));

		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
			bookService.borrowBook(1, 3);
		});

		assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
		assertEquals("Book is already borrowed", ex.getReason());
	}

	@Test
	void returnBookSuccess() {
		Book book = new Book();
		book.setBorrowed(true);
		book.setBorrower(new Borrower());

		when(bookRepository.findById(1)).thenReturn(Optional.of(book));
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		Book result = bookService.returnBook(1);

		assertNotNull(result);
		assertFalse(result.isBorrowed());
		assertNull(result.getBorrower());
	}

	@Test
	void returnBookFailed() {
		Book book = new Book();
		book.setBorrowed(false);

		when(bookRepository.findById(1)).thenReturn(Optional.of(book));

		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
			bookService.returnBook(1);
		});

		assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
		assertEquals("Book is not borrowed", ex.getReason());
	}

}
