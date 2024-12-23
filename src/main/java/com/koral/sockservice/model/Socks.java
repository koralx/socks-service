package com.koral.sockservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "socks")
public class Socks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer cottonPart;

    @Column(nullable = false)
    private Integer quantity;

    public Socks() {
    }

    public Socks(String color, Integer cottonPart, Integer quantity) {
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Socks)) return false;
        final Socks other = (Socks) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        if (this$color == null ? other$color != null : !this$color.equals(other$color)) return false;
        final Object this$cottonPart = this.getCottonPart();
        final Object other$cottonPart = other.getCottonPart();
        if (this$cottonPart == null ? other$cottonPart != null : !this$cottonPart.equals(other$cottonPart))
            return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Socks;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $color = this.getColor();
        result = result * PRIME + ($color == null ? 43 : $color.hashCode());
        final Object $cottonPart = this.getCottonPart();
        result = result * PRIME + ($cottonPart == null ? 43 : $cottonPart.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        return result;
    }
}