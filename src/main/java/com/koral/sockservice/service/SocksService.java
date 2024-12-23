package com.koral.sockservice.service;

import com.koral.sockservice.dto.SocksRequestDto;
import com.koral.sockservice.model.Socks;
import com.koral.sockservice.exception.CustomException;
import com.koral.sockservice.repository.SocksRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SocksService {

    private final SocksRepository socksRepository;

    SocksService(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }

    @Transactional
    public void registerIncome(SocksRequestDto requestDto) {
        Socks socks = socksRepository.findByColorAndCottonPart(requestDto.getColor(), requestDto.getCottonPart())
                .orElse(new Socks(requestDto.getColor(), requestDto.getCottonPart(), 0));
        socks.setQuantity(socks.getQuantity() + requestDto.getQuantity());
        socksRepository.save(socks);
    }

    @Transactional
    public void registerOutcome(SocksRequestDto requestDto) {
        Socks socks = socksRepository.findByColorAndCottonPart(requestDto.getColor(), requestDto.getCottonPart())
                .orElseThrow(() -> new CustomException("Socks not found"));

        if (socks.getQuantity() < requestDto.getQuantity()) {
            throw new CustomException("Not enough socks in stock");
        }

        socks.setQuantity(socks.getQuantity() - requestDto.getQuantity());
        socksRepository.save(socks);
    }

    @Transactional()
    public Map<String, Object> getSocks(String color, String operation, Integer cottonPart) {
        Map<String, Object> result = new HashMap<>();
        Long total = socksRepository.countByFilter(color, operation, cottonPart);
        result.put("total", total);
        return result;
    }

    @Transactional
    public void updateSocks(Long id, SocksRequestDto requestDto) {
        Socks socks = socksRepository.findById(id)
                .orElseThrow(() -> new CustomException("Socks not found"));

        socks.setColor(requestDto.getColor());
        socks.setCottonPart(requestDto.getCottonPart());
        socks.setQuantity(requestDto.getQuantity());

        socksRepository.save(socks);
    }
}
