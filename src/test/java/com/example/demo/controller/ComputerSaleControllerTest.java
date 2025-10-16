package com.example.demo.controller;

import com.example.demo.model.entity.ComputerSale;
import com.example.demo.repository.ComputerSaleRepository;
import com.example.demo.service.ComputerSaleService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComputerSaleController.class)
class ComputerSaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComputerSaleRepository repository;

    @MockitoBean
    private ComputerSaleService computerSaleService;

    @Test
    void testExportHtml() throws Exception {
        ComputerSale sale = new ComputerSale();
        sale.setId(1L);
        sale.setCountry("Czech Republic");
        sale.setTimescale("2010 Q3");
        sale.setVendor("Dell");
        sale.setUnits(100L);

        Mockito.when(repository.findAll()).thenReturn(List.of(sale));

        mockMvc.perform(get("/computer-sale")
                .param("format", "HTML"))
            .andExpect(status().isOk());
    }

    @Test
    void testImportCsv() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.csv",
            "text/csv",
            "country,timescale,vendor,units\nCzech Republic,2010 Q3,Dell,100".getBytes()
        );

        Mockito.when(repository.saveAll(Mockito.any())).thenReturn(Mockito.anyList());

        mockMvc.perform(multipart("/computer-sale/csv")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk());
    }
}
