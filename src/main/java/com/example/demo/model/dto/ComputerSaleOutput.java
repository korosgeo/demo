package com.example.demo.model.dto;

/**
 * DTO representing the output of computer sales data.
 */
public record ComputerSaleOutput(
    String vendor,
    Long units,
    Double share
) {}
