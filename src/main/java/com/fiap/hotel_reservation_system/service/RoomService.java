package com.fiap.hotel_reservation_system.service;


import com.fiap.hotel_reservation_system.dto.RoomRequestDTO;
import com.fiap.hotel_reservation_system.dto.RoomResponseDTO;
import com.fiap.hotel_reservation_system.exception.BusinessException;
import com.fiap.hotel_reservation_system.exception.ResourceNotFoundException;
import com.fiap.hotel_reservation_system.model.Room;
import com.fiap.hotel_reservation_system.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<RoomResponseDTO> findAll() {
        return roomRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDTO> findByStatus(String status) {
        return roomRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RoomResponseDTO findById(String id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return toDTO(room);
    }

    public RoomResponseDTO create(RoomRequestDTO dto) {
        // Check if room number already exists
        roomRepository.findByNumber(dto.getNumber())
                .ifPresent(room -> {
                    throw new BusinessException("Room number already exists: " + dto.getNumber());
                });

        Room room = new Room();
        room.setNumber(dto.getNumber());
        room.setType(dto.getType());
        room.setCapacity(dto.getCapacity());
        room.setPricePerNight(dto.getPricePerNight());
        room.setStatus("ACTIVE");

        Room savedRoom = roomRepository.save(room);
        return toDTO(savedRoom);
    }

    public void deactivate(String id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        room.setStatus("INACTIVE");
        roomRepository.save(room);
    }

    private RoomResponseDTO toDTO(Room room) {
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(room.getId());
        dto.setNumber(room.getNumber());
        dto.setType(room.getType());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setStatus(room.getStatus());
        return dto;
    }

    public Room findRoomEntityById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }
}