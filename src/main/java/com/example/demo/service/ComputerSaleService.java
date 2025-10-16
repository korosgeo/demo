package com.example.demo.service;

import com.example.demo.model.dto.ComputerSaleOutput;
import com.example.demo.model.entity.ComputerSale;
import com.example.demo.model.ExportFormat;
import com.example.demo.repository.ComputerSaleRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Service class for managing computer sales data.
 */
@Service
@RequiredArgsConstructor
public class ComputerSaleService {

    private static final Logger logger = LoggerFactory.getLogger(ComputerSaleService.class);

    private final ComputerSaleRepository computerSaleRepository;

    public void importCsv(MultipartFile file) {
        if (file.isEmpty()) {
            logger.warn("Uploaded file is empty");
            return;
        }

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<ComputerSale> computerSales = new ArrayList<>();
            String[] nextLine;
            csvReader.readNext();

            while ((nextLine = csvReader.readNext()) != null) {
                ComputerSale computerSale = new ComputerSale();
                computerSale.setCountry(nextLine[0]);
                computerSale.setTimescale(nextLine[1]);
                computerSale.setVendor(nextLine[2]);

                Long unitsValue = null;
                try {
                    unitsValue = Long.parseLong(nextLine[3]);
                } catch (NumberFormatException e) {
                    logger.warn("Unable to parse units value: {}", nextLine[3]);
                }
                computerSale.setUnits(unitsValue);
                computerSales.add(computerSale);
            }

            computerSaleRepository.saveAll(computerSales);
        } catch (IOException | CsvValidationException e) {
            logger.error("Error while reading csv file: {}", e.getMessage());
        }
    }

    public void export(ExportFormat format, HttpServletResponse response) throws IOException {
        switch (format) {
            case HTML:
                exportHtml(response);
                break;
            case CSV, EXCEL:
            default:
                logger.warn("Export format {} is not supported yet", format);
        }
    }

    private void exportHtml(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("<html>");
            writer.println("<head><title>Computer Sales Export</title></head>");
            writer.println("<body>");
            writer.println("<h2>IDC Data Export</h2>");
            writer.println("<table border='1'>");

            // Table header
            writer.println("<tr><th>Vendor</th><th>Units</th><th>Share</th></tr>");

            Map<String, Long> vendorToUnits = computerSaleRepository.findAll().stream().collect(Collectors.groupingBy(
                ComputerSale::getVendor,
                Collectors.summingLong(ComputerSale::getUnits)
            ));

            Long totalUnits = vendorToUnits.values().stream().reduce(0L, Long::sum);
            List<ComputerSaleOutput> sales = vendorToUnits.entrySet()
                .stream()
                .map(entry -> new ComputerSaleOutput(entry.getKey(), entry.getValue(), entry.getValue() * 100.0 / totalUnits))
                .sorted(Comparator.comparing(ComputerSaleOutput::vendor)
                    .thenComparing(ComputerSaleOutput::units))
                .toList();


            for (ComputerSaleOutput sale : sales) {
                writer.println("<tr>");
                writer.printf("<td>%s</td>", escapeHtml(sale.vendor()));
                writer.printf("<td>%s</td>", sale.units());
                writer.printf("<td>%.2f%%</td>", sale.share());
                writer.println("</tr>");
            }

            writer.println("</table>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;").replace("'", "&#39;");
    }

    public void deleteData() {
        computerSaleRepository.deleteAll();
    }
}
