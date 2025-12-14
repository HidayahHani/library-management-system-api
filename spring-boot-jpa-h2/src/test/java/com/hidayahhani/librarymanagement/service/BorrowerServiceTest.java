package com.hidayahhani.librarymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hidayahhani.librarymanagement.entity.Borrower;
import com.hidayahhani.librarymanagement.repo.BorrowerRepository;
import com.hidayahhani.librarymanagement.service.BorrowerService;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

	@Mock
	private BorrowerRepository borrowerRepository;

	@InjectMocks
	private BorrowerService borrowerService;

	@Test
	void testRegisterBorrowerSuccess() {
		Borrower borrower = new Borrower();
		borrower.setName("Emily");
		borrower.setEmail("emily00@gmail.com");

		when(borrowerRepository.save(borrower)).thenReturn(borrower);

		Borrower savedBorrower = borrowerService.registerBorrower(borrower);

		assertNotNull(savedBorrower);
		assertEquals("Emily", savedBorrower.getName());
		assertEquals("emily00@gmail.com", savedBorrower.getEmail());
	}

}
