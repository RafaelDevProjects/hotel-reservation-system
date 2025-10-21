package com.fiap.hotel_reservation_system.repository;

import com.fiap.hotel_reservation_system.model.Reservation;
import com.fiap.hotel_reservation_system.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findByRoom(Room room);
    List<Reservation> findByStatus(String status);

    @Query("SELECT r FROM Reservation r WHERE r.room = :room AND r.status IN ('CREATED', 'CHECKED_IN') " +
            "AND ((r.checkinExpected <= :checkout AND r.checkoutExpected >= :checkin))")
    List<Reservation> findOverlappingReservations(@Param("room") Room room,
                                                  @Param("checkin") LocalDate checkin,
                                                  @Param("checkout") LocalDate checkout);

    @Query("SELECT r FROM Reservation r WHERE r.id = :id AND r.status = 'CREATED'")
    Optional<Reservation> findByIdAndCreatedStatus(String id);

    @Query("SELECT r FROM Reservation r WHERE r.id = :id AND r.status = 'CHECKED_IN'")
    Optional<Reservation> findByIdAndCheckedInStatus(String id);
}
