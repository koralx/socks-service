package com.koral.sockservice.controller;


import com.koral.sockservice.service.FileProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/socks/batch")
public class BatchController {

    private final FileProcessingService fileProcessingService;

    BatchController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @PostMapping
    public ResponseEntity<String> uploadBatch(@RequestParam("file") MultipartFile file) {
        try {
            fileProcessingService.processFile(file);
            return ResponseEntity.ok("Данные успешно загружены.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }
}
