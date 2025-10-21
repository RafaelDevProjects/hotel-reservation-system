package com.fiap.hotel_reservation_system.controller;

import com.fiap.hotel_reservation_system.dto.RoomRequestDTO;
import com.fiap.hotel_reservation_system.dto.RoomResponseDTO;
import com.fiap.hotel_reservation_system.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "1. Quartos", description = "APIs para gerenciamento de quartos do hotel")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    @Operation(
            summary = "Listar todos os quartos",
            description = "Retorna todos os quartos cadastrados no sistema com seus detalhes"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Quartos listados com sucesso",
            content = @Content(schema = @Schema(implementation = RoomResponseDTO.class))
    )
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        List<RoomResponseDTO> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar quarto por ID",
            description = "Retorna um quarto específico baseado no ID fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Quarto encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = RoomResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quarto não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<RoomResponseDTO> getRoomById(
            @Parameter(description = "ID único do quarto", example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            @PathVariable String id) {
        RoomResponseDTO room = roomService.findById(id);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Buscar quartos por status",
            description = "Retorna quartos filtrados por status (ACTIVE ou INACTIVE)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Quartos filtrados com sucesso",
            content = @Content(schema = @Schema(implementation = RoomResponseDTO.class))
    )
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByStatus(
            @Parameter(description = "Status do quarto", example = "ACTIVE")
            @PathVariable String status) {
        List<RoomResponseDTO> rooms = roomService.findByStatus(status);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    @Operation(
            summary = "Criar novo quarto",
            description = "Cadastra um novo quarto no sistema com os dados fornecidos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Quarto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = RoomResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            )
    })
    public ResponseEntity<RoomResponseDTO> createRoom(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do quarto a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RoomRequestDTO.class))
            )
            @Valid @RequestBody RoomRequestDTO roomDTO) {
        RoomResponseDTO createdRoom = roomService.create(roomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(
            summary = "Desativar quarto",
            description = "Altera o status do quarto para INACTIVE, impedindo novas reservas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Quarto desativado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quarto não encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deactivateRoom(
            @Parameter(description = "ID único do quarto", example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            @PathVariable String id) {
        roomService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}