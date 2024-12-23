package com.koral.sockservice.dto;

import lombok.Data;
import lombok.Value;

@Data
public class SocksRequestDto {
    private String color;
    private Integer cottonPart;
    private Integer quantity;

    public SocksRequestDto() {
    }

    public SocksRequestDto(String color, Integer cottonPart, Integer quantity) {
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }
}
