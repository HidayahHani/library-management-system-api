package com.hidayahhani.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hidayahhani.librarymanagement.entity.Borrower;
import com.hidayahhani.librarymanagement.repo.BorrowerRepository;

@Service
public class BorrowerService {
	@Autowired
	private BorrowerRepository borrowerRepository;

	public Borrower registerBorrower(Borrower borrower) {
		return borrowerRepository.save(borrower);
	}
}
