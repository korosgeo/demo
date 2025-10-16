package com.example.demo.controller;

import com.example.demo.model.ExportFormat;
import com.example.demo.service.ComputerSaleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing computer sales data.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ComputerSaleController.URI)
public class ComputerSaleController {

    public static final String URI = "/computer-sale";

    private static final Logger logger = LoggerFactory.getLogger(ComputerSaleController.class);

    private final ComputerSaleService computerSaleService;

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void importCsv(@RequestParam("file") MultipartFile file) {
        logger.info("Started importing csv file {}", file.getOriginalFilename());
        computerSaleService.importCsv(file);
        logger.info("Finished importing csv file {}", file.getOriginalFilename());
    }

    @GetMapping
    void export(@RequestParam("format") ExportFormat format, HttpServletResponse response) throws IOException {
        logger.info("Exporting data in {} format", format);
        computerSaleService.export(format, response);
        logger.info("Exported data in {} format", format);
    }

    @DeleteMapping
    void deleteData() {
        logger.info("Deleting all the data for computer sales");
        computerSaleService.deleteData();
        logger.info("Deleted all the data for computer sales");
    }
}
