package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a computer sale record.
 */
@Getter
@Setter
@Entity(name = "computer_sales")
public class ComputerSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "timescale", length = 50)
    private String timescale;

    @Column(name = "vendor", length = 100)
    private String vendor;

    @Column(name = "units")
    private Long units;

}
