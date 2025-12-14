package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Borrower;
import com.example.demo.repo.BorrowerRepository;

@Service
public class BorrowerService {
	@Autowired
	private BorrowerRepository borrowerRepository;

	public Borrower registerBorrower(Borrower borrower) {
		return borrowerRepository.save(borrower);
	}
}
