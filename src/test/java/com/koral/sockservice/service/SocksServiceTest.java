package com.koral.sockservice.service;

import com.koral.sockservice.dto.SocksRequestDto;
import com.koral.sockservice.model.Socks;
import com.koral.sockservice.repository.SocksRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SocksServiceTest {

    @InjectMocks
    private SocksService socksService;

    @Mock
    private SocksRepository socksRepository;

    @Test
    void registerIncome() {

    }

    @Test
    void registerOutcome() {
    }

    @Test
    void getSocks() {

    }

    @Test
    void updateSocks() {
    }
}
