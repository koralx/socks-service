package com.koral.sockservice.util;

import com.koral.sockservice.exception.ParserException;
import com.koral.sockservice.model.Socks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CsvParserTest {

    @InjectMocks
    CsvSocksParser csvSocksParser;

    @Test
    public void parseSocks_getParsedCsvAsArrayList_success() {
        ArrayList<Socks> mockSocksArrayList = new ArrayList<>(List.of(
                new Socks("Желтый",36, 68 ),
                new Socks("Синий",89, 56 ),
                new Socks("Красный",41, 45 ),
                new Socks("Желтый",82, 31 ),
                new Socks("Синий"	,57, 65 ),
                new Socks("Красный",62, 44 ),
                new Socks("Желтый",31, 39 ),
                new Socks("Синий",6, 69),
                new Socks("Красный",3, 42),
                new Socks("Желтый",24, 47 ),
                new Socks("Синий",7, 1),
                new Socks("Красный",88, 42 ),
                new Socks("Желтый",82, 66 ),
                new Socks("Синий",74, 67 ),
                new Socks("Красный",64, 59 ),
                new Socks("Желтый",57, 12 ),
                new Socks("Синий",85, 55 ),
                new Socks("Красный",89, 34 ),
                new Socks("Желтый",16, 33 ),
                new Socks("Синий",42, 1)
        ));


        String csvFileData = null;
        try {
            csvFileData = Files.readString(Paths.get("src/test/resources/sample_data/socks_test.csv"), Charset.forName("Windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] csvConvertedBytes = Objects.requireNonNull(csvFileData).getBytes(Charset.forName("Windows-1251"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.csv", "text/csv", csvConvertedBytes);

        ArrayList<Socks> csvParsedSocksArrayList = csvSocksParser.parseSocks(mockMultipartFile);

        assertArrayEquals(mockSocksArrayList.toArray(), csvParsedSocksArrayList.toArray());
    }

    @Test
    public void parseSocks_emptyFile_ExceptionParse() {
        String csvFileData = null;
        try {
            csvFileData = Files.readString(Paths.get("src/test/resources/sample_data/empty_file.csv"), Charset.forName("Windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] csvConvertedBytes = csvFileData.getBytes(Charset.forName("Windows-1251"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.csv", "text/csv", csvConvertedBytes);

        Exception thrown = assertThrows(ParserException.class, () ->
                csvSocksParser.parseSocks(mockMultipartFile)
        );
        assertTrue(thrown.getMessage().contains("Файл пустой"));
    }

    @Test
    public void parseSocks_unsupportedFileFormat_ExceptionParse() {
        String csvFileData = "some bytes";

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.unknown_type", "text/csv", csvFileData.getBytes());

        Exception thrown = assertThrows(ParserException.class, () ->
                csvSocksParser.parseSocks(mockMultipartFile)
        );

        assertTrue(thrown.getMessage().contains("Неподдерживаемый формат файла"));
    }

}
