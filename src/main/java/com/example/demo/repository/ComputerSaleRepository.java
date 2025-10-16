package com.example.demo.repository;

import com.example.demo.model.entity.ComputerSale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing ComputerSale entities.
 */
@Repository
public interface ComputerSaleRepository extends JpaRepository<ComputerSale, Long> {
}
