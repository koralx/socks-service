package com.koral.sockservice.util;

import com.koral.sockservice.exception.CustomException;
import com.koral.sockservice.model.Socks;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExcelParserTest {
    @Test
    @SneakyThrows
    public void testXlsxParser() {
        ExcelSocksParser excelParser = new ExcelSocksParser();

        File csvFile = new File("socks_test.csv");
        byte[] fileCsvContent = null;
        fileCsvContent = Files.readAllBytes(csvFile.toPath());

        MockMultipartFile mockCsvMultipartFile = new MockMultipartFile(
                "file",
                "socks_test.csv",
                "text.csv",
                fileCsvContent
        );

        ArrayList<Socks> mockSocksArrayList = new ArrayList<>();

        InputStream inputStream = mockCsvMultipartFile.getInputStream();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "Windows-1251"))) {
            String line = in.readLine();

            while ((line = in.readLine()) != null) {
                String[] parts = line.split(";");
                String color = parts[0];
                Integer cottonValue = Integer.parseInt(parts[1]);
                Integer quantity = Integer.parseInt(parts[2]);
                Socks socks = new Socks();
                socks.setColor(color);
                socks.setCottonPart(cottonValue);
                socks.setQuantity(quantity);
                mockSocksArrayList.add(socks);
            }

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }


        File xslxFile = new File("socks_test.xlsx");
        byte[] fileXlsxContent = null;
        fileXlsxContent = Files.readAllBytes(xslxFile.toPath());

        MockMultipartFile mockExcelMultipartFile = new MockMultipartFile(
                "file",
                "socks_test.xlsx",
                "application/vnd.ms-excel",
                fileXlsxContent
        );

        ArrayList<Socks> excelParsedSocksArrayList = excelParser.parseSocks(mockExcelMultipartFile);

        assertTrue(Arrays.equals(mockSocksArrayList.toArray(), excelParsedSocksArrayList.toArray()));
    }
}
