package com.fiap.hotel_reservation_system.repository;

import com.fiap.hotel_reservation_system.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Optional<Room> findByNumber(Integer number);
    List<Room> findByStatus(String status);
    List<Room> findByTypeAndStatus(String type, String status);
}
