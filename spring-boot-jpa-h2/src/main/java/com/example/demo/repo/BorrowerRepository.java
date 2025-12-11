package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {

}
