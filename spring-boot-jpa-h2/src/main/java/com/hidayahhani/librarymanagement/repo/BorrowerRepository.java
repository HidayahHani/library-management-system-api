package com.hidayahhani.librarymanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hidayahhani.librarymanagement.entity.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {

}
