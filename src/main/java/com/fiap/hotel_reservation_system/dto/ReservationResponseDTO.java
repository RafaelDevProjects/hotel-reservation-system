package com.fiap.hotel_reservation_system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class ReservationResponseDTO {
    private String id;
    private RoomResponseDTO room;
    private String guestName;
    private LocalDate checkinExpected;
    private LocalDate checkoutExpected;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public RoomResponseDTO getRoom() { return room; }
    public void setRoom(RoomResponseDTO room) { this.room = room; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public LocalDate getCheckinExpected() { return checkinExpected; }
    public void setCheckinExpected(LocalDate checkinExpected) { this.checkinExpected = checkinExpected; }

    public LocalDate getCheckoutExpected() { return checkoutExpected; }
    public void setCheckoutExpected(LocalDate checkoutExpected) { this.checkoutExpected = checkoutExpected; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}