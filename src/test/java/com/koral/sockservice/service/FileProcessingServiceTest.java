package com.koral.sockservice.service;

import com.koral.sockservice.exception.ProcessingFileException;
import com.koral.sockservice.model.Socks;
import com.koral.sockservice.repository.SocksRepository;
import com.koral.sockservice.util.CsvSocksParser;
import com.koral.sockservice.util.XlsxSocksParser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileProcessingServiceTest {
    @InjectMocks
    private FileProcessingService fileProcessingService;

    @Mock
    private SocksRepository socksRepository;

    @Mock
    private CsvSocksParser csvSocksParser;
    @Mock
    private XlsxSocksParser xlsxSocksParser;

    @Test
    void processFile_ParseCsv_Success() {
        String csvFileData = null;
        try {
            csvFileData = Files.readString(Paths.get("src/test/resources/sample_data/socks_test.csv"), Charset.forName("Windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] csvConvertedBytes = csvFileData.getBytes(Charset.forName("Windows-1251"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.csv", "text/csv", csvConvertedBytes);

        when(csvSocksParser.parseSocks(any(MultipartFile.class)))
                .thenReturn(new ArrayList<>(List.of(
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
                )));


        ArrayList<Socks> parsedSocksList = fileProcessingService.processFile(mockMultipartFile);

        for (Socks socks : parsedSocksList) {
            verify(socksRepository, times(1)).save(socks);
        }

    }

    @Test
    void processFile_ParseXlsx_Success() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Row title = sheet.createRow(1);
        title.createCell(0).setCellValue("Цвет");
        title.createCell(1).setCellValue("Содержание хлопка %");
        title.createCell(2).setCellValue("Количество");

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("Желтый");
        row1.createCell(1).setCellValue(36);
        row1.createCell(2).setCellValue(68);

        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Синий");
        row2.createCell(1).setCellValue(89);
        row2.createCell(2).setCellValue(56);


        MockMultipartFile mockMultipartFile = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] bytes = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            mockMultipartFile = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", inputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        when(xlsxSocksParser.parseSocks(any(MultipartFile.class)))
                .thenReturn(new ArrayList<>(List.of(new Socks("Желтый", 36, 68), new Socks("Синий", 89, 56))));


        ArrayList<Socks> parsedSocksList = fileProcessingService.processFile(mockMultipartFile);

        verify(socksRepository, times(1)).save(parsedSocksList.get(0));
        verify(socksRepository, times(1)).save(parsedSocksList.get(1));
    }

    @Test
    void processFile_UploadUndefinedFile_ProcessingFileException() {
        Workbook workbook = new XSSFWorkbook();

        MockMultipartFile mockMultipartFile = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] bytes = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            mockMultipartFile = new MockMultipartFile("file", "test", "application/vnd.ms-excel", inputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MockMultipartFile finalMockMultipartFile = mockMultipartFile;
        Exception thrown = assertThrows(ProcessingFileException.class, () ->
                fileProcessingService.processFile(finalMockMultipartFile)
        );

        assertTrue(thrown.getMessage().contains("Поддерживаются только"));
    }

    @Test
    void processFile_UploadEmptyFile_ProcessingFileException() {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", (byte[]) null);

        Exception thrown = assertThrows(ProcessingFileException.class, () ->
                fileProcessingService.processFile(mockMultipartFile)
        );

        assertTrue(thrown.getMessage().contains("Отствует файл"));
    }

    /*Test
    void processFile_UploadWithoutNameFile_ExceptionParse() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        MockMultipartFile mockMultipartFile = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] bytes = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            mockMultipartFile = new MockMultipartFile("", "", "application/vnd.ms-excel", inputStream);
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(excelSocksParser.parseSocks(any(MultipartFile.class)))
                .thenReturn(new ArrayList<>(List.of(new Socks("Желтый", 36, 68), new Socks("Синий", 89, 56))));

        MockMultipartFile finalMockMultipartFile = mockMultipartFile;
        Exception thrown = assertThrows(ProcessingFileException.class, () ->
                fileProcessingService.processFile(finalMockMultipartFile)
        );

        assertTrue(thrown.getMessage().contains("Файл не может"));
    }*/

}
