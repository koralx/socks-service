package com.koral.sockservice.util;

import com.koral.sockservice.exception.ParserException;
import com.koral.sockservice.model.Socks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class XlsxParserTest {

    @InjectMocks
    XlsxSocksParser xlsxSocksParser;

    @Test
    public void parseSocks_getParsedXlsxAsArrayList_success() {

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

        byte[] fileXlsxContent = null;
        try {
            fileXlsxContent = Files.readAllBytes(Paths.get("src/test/resources/sample_data/socks_test.xlsx"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MockMultipartFile mockXlsxMultipartFile = new MockMultipartFile(
                "file",
                "socks_test.xlsx",
                "application/vnd.ms-excel",
                fileXlsxContent
        );

        ArrayList<Socks> excelParsedSocksArrayList = xlsxSocksParser.parseSocks(mockXlsxMultipartFile);

        assertArrayEquals(mockSocksArrayList.toArray(), excelParsedSocksArrayList.toArray());

    }

    @Test
    public void parseSocks_emptyFile_ExceptionParse() {
        byte[] fileXlsxEmptyContent = null;

        MockMultipartFile mockXlsxMultipartFile = new MockMultipartFile(
                "file",
                "empty_file.xlsx",
                "application/vnd.ms-excel",
                fileXlsxEmptyContent
        );

        Exception thrown = assertThrows(ParserException.class, () ->
                xlsxSocksParser.parseSocks(mockXlsxMultipartFile)
        );

        assertTrue(thrown.getMessage().contains("Файл пустой"));
    }

    @Test
    public void parseSocks_unsupportedFileFormat_ExceptionParse() {
        String xlsxFileData = "some bytes";

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.unknown_type", "text/csv", xlsxFileData.getBytes());

        Exception thrown = assertThrows(ParserException.class, () ->
                xlsxSocksParser.parseSocks(mockMultipartFile)
        );

        assertTrue(thrown.getMessage().contains("Неподдерживаемый формат файла"));
    }
}
