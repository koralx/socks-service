package com.koral.sockservice.service;

import com.koral.sockservice.exception.ProcessingFileException;
import com.koral.sockservice.model.Socks;
import com.koral.sockservice.repository.SocksRepository;
import com.koral.sockservice.util.CsvSocksParser;
import com.koral.sockservice.util.ExcelSocksParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class FileProcessingService {

    private final SocksRepository socksRepository;

    private final CsvSocksParser csvParser;
    private final ExcelSocksParser excelParser;

    private final Logger logger = Logger.getLogger(FileProcessingService.class.getName());

    public void processFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        logger.info("Начало обработки файла: " + fileName);

        if (fileName == null || fileName.isEmpty())
            throw new ProcessingFileException("Файл не может иметь пустое название.");

        if (fileName.endsWith(".csv") == fileName.endsWith(".xlsx"))
            throw new ProcessingFileException("Поддерживаются только .xlsx и .csv форматы файлов.");


        ArrayList<Socks> parsedSocksList = null;

        if (fileName.endsWith(".csv"))
            parsedSocksList = csvParser.parseSocks(file);

        if (fileName.endsWith(".xlsx"))
            parsedSocksList = excelParser.parseSocks(file);

        if (parsedSocksList == null)
            throw new ProcessingFileException("Не удалось извлечь данные из файла.");

        for (Socks parsedSocks : parsedSocksList) {
            Socks socks = socksRepository.findByColorAndCottonPart(parsedSocks.getColor(), parsedSocks.getCottonPart())
                    .orElse(new Socks(parsedSocks.getColor(), parsedSocks.getCottonPart(), 0));
            socks.setQuantity(socks.getQuantity() + parsedSocks.getQuantity());
            socksRepository.save(socks);
        }

    }
}
