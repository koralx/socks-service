package com.koral.sockservice.service;

import com.koral.sockservice.dto.SocksRequestDto;
import com.koral.sockservice.model.Socks;
import com.koral.sockservice.repository.SocksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SocksServiceTest {

    @InjectMocks
    private SocksService socksService;

    @Mock
    private SocksRepository socksRepository;

    @Test
    void registerIncome() {
        SocksRequestDto socksRequestDto = new SocksRequestDto("red", 80, 10);
        Socks socks = new Socks("red", 80, 10);
        when(socksRepository.findByColorAndCottonPart("red", 80)).thenReturn(Optional.of(socks));
        socksService.registerIncome(socksRequestDto);
        assertEquals(20, socks.getQuantity());
        verify(socksRepository, times(1)).save(socks);
    }

    @Test
    void registerOutcome() {
        SocksRequestDto requestDto = new SocksRequestDto("red", 80, 5);
        Socks socks = new Socks("red", 80, 10);
        when(socksRepository.findByColorAndCottonPart("red", 80)).thenReturn(Optional.of(socks));

        socksService.registerOutcome(requestDto);

        assertEquals(5, socks.getQuantity());
        verify(socksRepository, times(1)).save(socks);
    }

    @Test
    void getSocks() {
        when(socksRepository.countByFilter("red", "moreThan", 50)).thenReturn(15L);
        Map<String, Object> result = socksService.getSocks("red", "moreThan", 50);
        Long count = (Long) result.get("total");
        assertEquals(15, count);
        verify(socksRepository, times(1)).countByFilter("red", "moreThan", 50);
    }

    @Test
    void updateSocks() {
        SocksRequestDto socksRequestDto = new SocksRequestDto("red", 60, 15);
        Socks socks = new Socks("red", 80, 10);
        when(socksRepository.findById(1L)).thenReturn(Optional.of(socks));

        socksService.updateSocks(1L ,socksRequestDto);

        assertEquals(15, socks.getQuantity());
        assertEquals("red", socksRequestDto.getColor());
        assertEquals(60, socksRequestDto.getCottonPart());
        verify(socksRepository, times(1)).save(socks);
    }
}
