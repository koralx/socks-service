package com.koral.sockservice.controller;

import com.koral.sockservice.dto.SocksRequestDto;
import com.koral.sockservice.service.SocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class SocksController {

    private final SocksService socksService;

    // Регистрация прихода носков
    @PostMapping("/income")
    public ResponseEntity<String> registerIncome(@RequestBody SocksRequestDto requestDto) {
        socksService.registerIncome(requestDto);
        return ResponseEntity.ok("Income registered successfully.");
    }

    // Регистрация отпуска носков
    @PostMapping("/outcome")
    public ResponseEntity<String> registerOutcome(@RequestBody SocksRequestDto requestDto) {
        socksService.registerOutcome(requestDto);
        return ResponseEntity.ok("Outcome registered successfully.");
    }

    // Получение общего количества носков с фильтрацией
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSocks(@RequestParam(required = false) String color,
                                                        @RequestParam(required = false) String operation,
                                                        @RequestParam(required = false) Integer cottonPart) {
        Map<String, Object> result = socksService.getSocks(color, operation, cottonPart);
        return ResponseEntity.ok(result);
    }

    // Обновление данных носков
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSocks(@PathVariable Long id, @RequestBody SocksRequestDto requestDto) {
        socksService.updateSocks(id, requestDto);
        return ResponseEntity.ok("Socks updated successfully.");
    }
}
