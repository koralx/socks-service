package com.koral.sockservice.util;

import com.koral.sockservice.exception.CustomException;
import com.koral.sockservice.model.Socks;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.BufferedReader;
import java.io.File;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class CsvParserTest {

    Logger log = LoggerFactory.getLogger(CsvParserTest.class);

    @Test
    @SneakyThrows
    public void testCsvParser() {
        CsvSocksParser csvParser = new CsvSocksParser();

        File csvFile = new File("socks_test.csv");
        byte[] fileContent = null;
        fileContent = Files.readAllBytes(csvFile.toPath());

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "socks.csv",
                "text/csv",
                fileContent
        );

        ArrayList<Socks> mockSocksArrayList = new ArrayList<>();

        InputStream inputStream = mockMultipartFile.getInputStream();
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

        ArrayList<Socks> csvParsedSocksArrayList = csvParser.parseSocks(mockMultipartFile);

        assertTrue(Arrays.equals(mockSocksArrayList.toArray(), csvParsedSocksArrayList.toArray()));
    }
}
