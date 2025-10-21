package com.fiap.hotel_reservation_system.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ReservationRequestDTO {

    @NotBlank(message = "Room ID is mandatory")
    private String roomId;

    @NotBlank(message = "Guest name is mandatory")
    @Size(min = 2, max = 120, message = "Guest name must be between 2 and 120 characters")
    private String guestName;

    @NotNull(message = "Check-in date is mandatory")
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    private LocalDate checkinExpected;

    @NotNull(message = "Check-out date is mandatory")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkoutExpected;

    // Getters and Setters
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public LocalDate getCheckinExpected() { return checkinExpected; }
    public void setCheckinExpected(LocalDate checkinExpected) { this.checkinExpected = checkinExpected; }

    public LocalDate getCheckoutExpected() { return checkoutExpected; }
    public void setCheckoutExpected(LocalDate checkoutExpected) { this.checkoutExpected = checkoutExpected; }
}