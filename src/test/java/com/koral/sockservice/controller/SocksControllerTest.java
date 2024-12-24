package com.koral.sockservice.controller;

import com.koral.sockservice.dto.SocksRequestDto;
import com.koral.sockservice.service.SocksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SocksControllerTest {
    @Mock
    private SocksService socksService;

    @InjectMocks
    private SocksController socksController;

    // Controller Tests
    @Test
    void testRegisterIncomeController() {
        SocksRequestDto requestDto = new SocksRequestDto("red", 80, 10);
        doNothing().when(socksService).registerIncome(any(SocksRequestDto.class));

        ResponseEntity<String> response = socksController.registerIncome(requestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Income registered successfully.", response.getBody());
        verify(socksService, times(1)).registerIncome(any(SocksRequestDto.class));
    }

    @Test
    void testRegisterOutcomeController() {
        SocksRequestDto requestDto = new SocksRequestDto("red", 80, 5);
        doNothing().when(socksService).registerOutcome(requestDto);

        ResponseEntity<String> response = socksController.registerOutcome(requestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Outcome registered successfully.", response.getBody());
        verify(socksService, times(1)).registerOutcome(requestDto);
    }

    @Test
    void testGetSocksController() {
        Map<String, Object> result = Map.of("total", 15);
        when(socksService.getSocks("red", "moreThan", 50)).thenReturn(result);

        ResponseEntity<Map<String, Object>> response = socksController.getSocks("red", "moreThan", 50);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(result, response.getBody());
        verify(socksService, times(1)).getSocks("red", "moreThan", 50);
    }

    @Test
    void testUpdateSocksController() {
        SocksRequestDto requestDto = new SocksRequestDto("red", 80, 20);
        doNothing().when(socksService).updateSocks(1L, requestDto);

        ResponseEntity<String> response = socksController.updateSocks(1L, requestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Socks updated successfully.", response.getBody());
        verify(socksService, times(1)).updateSocks(1L, requestDto);
    }

    @Test
    void testGetSocksController_InvalidParameters() {
        when(socksService.getSocks(null, null, null)).thenReturn(Map.of("total", 0));

        ResponseEntity<Map<String, Object>> response = socksController.getSocks(null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().get("total"));
        verify(socksService, times(1)).getSocks(null, null, null);
    }

    @Test
    void testUpdateSocksController_NotFound() {
        SocksRequestDto requestDto = new SocksRequestDto("blue", 60, 15);
        doThrow(new RuntimeException("Socks not found"))
                .when(socksService).updateSocks(1L, requestDto);

        Exception exception = assertThrows(RuntimeException.class, () ->
                socksController.updateSocks(1L, requestDto));

        assertEquals("Socks not found", exception.getMessage());
    }
}
