package com.koral.sockservice.util;

import com.koral.sockservice.exception.ParserException;
import com.koral.sockservice.model.Socks;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class XlsxSocksParser implements SocksParser{

    private final Logger logger = LoggerFactory.getLogger(XlsxSocksParser.class);

    public ArrayList<Socks> parseSocks(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new ParserException("Файл пустой");
        }

        if (!Objects.requireNonNull(multipartFile.getOriginalFilename()).endsWith(".xlsx")) {
            throw new ParserException("Неподдерживаемый формат файла. Используйте .xlsx");
        }

        ArrayList<Socks> parsedData = new ArrayList<>(); // Список для хранения данных из файла

        try(InputStream inputStream = multipartFile.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            logger.info("Начало обработки файла {}", multipartFile.getOriginalFilename());
            Sheet sheet = workbook.getSheetAt(0);

            // Проходим по строкам
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Пропускаем заголовок
                }
                parsedData.add(parseRow(row));
                logger.info("Обработано строк: {}", parsedData.size());
            }
        } catch (IOException e) {
            throw new ParserException("Ошибка чтения файла: " + e.getMessage());
        }

        return parsedData;
    }

    private Socks parseRow(Row row) {
        Socks socks = new Socks();
        if (row.getCell(0) == null || !row.getCell(0).getCellType().equals(CellType.STRING)) {
            throw new ParserException("Неверный формат данных в колонке 1");
        }
        socks.setColor(row.getCell(0).getStringCellValue());

        if (row.getCell(1) == null || !row.getCell(1).getCellType().equals(CellType.NUMERIC)) {
            throw new ParserException("Неверный формат данных в колонке 2");
        }
        socks.setCottonPart((int) row.getCell(1).getNumericCellValue());

        if (row.getCell(2) == null || !row.getCell(2).getCellType().equals(CellType.NUMERIC)) {
            throw new ParserException("Неверный формат данных в колонке 3");
        }
        socks.setQuantity((int) row.getCell(2).getNumericCellValue());
        return socks;
    }
}
