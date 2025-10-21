package com.fiap.hotel_reservation_system.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class RoomRequestDTO {

    @NotNull(message = "Number is mandatory")
    @Positive(message = "Number must be positive")
    private Integer number;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotNull(message = "Capacity is mandatory")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Price per night is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerNight;

    // Getters and Setters
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
}