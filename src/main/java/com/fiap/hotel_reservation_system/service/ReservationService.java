package com.fiap.hotel_reservation_system.service;

import com.fiap.hotel_reservation_system.dto.ReservationRequestDTO;
import com.fiap.hotel_reservation_system.dto.ReservationResponseDTO;
import com.fiap.hotel_reservation_system.dto.RoomResponseDTO;
import com.fiap.hotel_reservation_system.exception.BusinessException;
import com.fiap.hotel_reservation_system.exception.ConflictException;
import com.fiap.hotel_reservation_system.exception.ResourceNotFoundException;
import com.fiap.hotel_reservation_system.model.Reservation;
import com.fiap.hotel_reservation_system.model.Room;
import com.fiap.hotel_reservation_system.repository.ReservationRepository;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomService roomService;

    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> findByStatus(String status) {
        return reservationRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO findById(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return toDTO(reservation);
    }

    @Transactional
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        validateDates(dto.getCheckinExpected(), dto.getCheckoutExpected());

        Room room = roomService.findRoomEntityById(dto.getRoomId());

        checkRoomAvailability(room, dto.getCheckinExpected(), dto.getCheckoutExpected());

        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setGuestName(dto.getGuestName());
        reservation.setCheckinExpected(dto.getCheckinExpected());
        reservation.setCheckoutExpected(dto.getCheckoutExpected());
        reservation.setStatus("CREATED");

        Reservation savedReservation = reservationRepository.save(reservation);
        return toDTO(savedReservation);
    }

    @Transactional
    public ReservationResponseDTO checkIn(String id) {
        Reservation reservation = reservationRepository.findByIdAndCreatedStatus(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation not found or not in CREATED status: " + id));

        if (reservation.getCheckinExpected().isAfter(LocalDate.now().plusDays(1))) {
            throw new BusinessException("Check-in is only allowed from 1 day before the expected date");
        }

        reservation.setStatus("CHECKED_IN");
        Reservation updatedReservation = reservationRepository.save(reservation);
        return toDTO(updatedReservation);
    }

    @Transactional
    public ReservationResponseDTO checkOut(String id) {
        Reservation reservation = reservationRepository.findByIdAndCheckedInStatus(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation not found or not in CHECKED_IN status: " + id));

        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckinExpected(),
                reservation.getCheckoutExpected()
        );
        BigDecimal totalAmount = reservation.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(nights));

        reservation.setTotalAmount(totalAmount);
        reservation.setStatus("CHECKED_OUT");

        Reservation updatedReservation = reservationRepository.save(reservation);
        return toDTO(updatedReservation);
    }

    @Transactional
    public ReservationResponseDTO cancel(String id) {
        Reservation reservation = reservationRepository.findByIdAndCreatedStatus(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation not found or not in CREATED status: " + id));

        reservation.setStatus("CANCELED");
        Reservation updatedReservation = reservationRepository.save(reservation);
        return toDTO(updatedReservation);
    }

    private void validateDates(LocalDate checkin, LocalDate checkout) {
        if (!checkout.isAfter(checkin)) {
            throw new BusinessException("Check-out date must be after check-in date");
        }

        if (checkin.isBefore(LocalDate.now())) {
            throw new BusinessException("Check-in date cannot be in the past");
        }
    }

    private void checkRoomAvailability(Room room, LocalDate checkin, LocalDate checkout) {
        List<Reservation> overlappingReservations = reservationRepository
                .findOverlappingReservations(room, checkin, checkout);

        if (!overlappingReservations.isEmpty()) {
            throw new ConflictException("Room not available for the selected dates");
        }
    }

    private ReservationResponseDTO toDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());

        RoomResponseDTO roomDTO = new RoomResponseDTO();
        roomDTO.setId(reservation.getRoom().getId());
        roomDTO.setNumber(reservation.getRoom().getNumber());
        roomDTO.setType(reservation.getRoom().getType());
        roomDTO.setCapacity(reservation.getRoom().getCapacity());
        roomDTO.setPricePerNight(reservation.getRoom().getPricePerNight());
        roomDTO.setStatus(reservation.getRoom().getStatus());

        dto.setRoom(roomDTO);
        dto.setGuestName(reservation.getGuestName());
        dto.setCheckinExpected(reservation.getCheckinExpected());
        dto.setCheckoutExpected(reservation.getCheckoutExpected());
        dto.setStatus(reservation.getStatus());
        dto.setTotalAmount(reservation.getTotalAmount());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());

        return dto;
    }
}
