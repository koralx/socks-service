package com.koral.sockservice.util;

import com.koral.sockservice.exception.ParserException;
import com.koral.sockservice.model.Socks;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;

@Component
public class CsvSocksParser implements SocksParser {

    private final Logger logger = LoggerFactory.getLogger(CsvSocksParser.class);

    @SneakyThrows
    public ArrayList<Socks> parseSocks(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new ParserException("Файл пустой");
        }

        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            throw new ParserException("Неподдерживаемый формат файла. Используйте .csv");
        }

        ArrayList<Socks> parsedData = new ArrayList<>();

        InputStream inputStream = multipartFile.getInputStream();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "Windows-1251"))) {
            logger.info("Начало обработки файла {}", multipartFile.getOriginalFilename());
            String line = in.readLine();

            while ((line = in.readLine()) != null) {
                String[] parts = line.split(";");
                parsedData.add(parseSocks(parts));
                logger.info("Обработано строк: {}", parsedData.size());
            }

        } catch (IOException e) {
            throw new ParserException("Ошибка чтения файла: " + e.getMessage());
        }

        return parsedData;
    }

    private Socks parseSocks(String[] parts) {
        Socks socks = new Socks();
        if (parts.length != 3) {
            throw new ParserException("Ошибка обработки строки");
        }
        socks.setColor(parts[0]);
        socks.setCottonPart(Integer.parseInt(parts[1]));
        socks.setQuantity(Integer.parseInt(parts[2]));
        return socks;
    }
}
