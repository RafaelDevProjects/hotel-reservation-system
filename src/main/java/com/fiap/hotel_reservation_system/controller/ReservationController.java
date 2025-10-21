package com.fiap.hotel_reservation_system.controller;

import com.fiap.hotel_reservation_system.dto.ReservationRequestDTO;
import com.fiap.hotel_reservation_system.dto.ReservationResponseDTO;
import com.fiap.hotel_reservation_system.service.ReservationService;
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
@RequestMapping("/api/reservations")
@Tag(name = "2. Reservas", description = "APIs para gerenciamento de reservas do hotel")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @Operation(
            summary = "Listar todas as reservas",
            description = "Retorna todas as reservas do sistema com seus detalhes completos"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Reservas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReservationResponseDTO.class))
    )
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Retorna uma reserva específica baseada no ID fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva não encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<ReservationResponseDTO> getReservationById(
            @Parameter(description = "ID único da reserva", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id) {
        ReservationResponseDTO reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Buscar reservas por status",
            description = "Retorna reservas filtradas por status (CREATED, CHECKED_IN, CHECKED_OUT, CANCELED)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Reservas filtradas com sucesso",
            content = @Content(schema = @Schema(implementation = ReservationResponseDTO.class))
    )
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByStatus(
            @Parameter(description = "Status da reserva", example = "CREATED")
            @PathVariable String status) {
        List<ReservationResponseDTO> reservations = reservationService.findByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    @Operation(
            summary = "Criar nova reserva",
            description = "Cria uma nova reserva validando disponibilidade do quarto e datas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva criada com sucesso",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito - Quarto não disponível para as datas selecionadas",
                    content = @Content
            )
    })
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da reserva a ser criada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReservationRequestDTO.class))
            )
            @Valid @RequestBody ReservationRequestDTO reservationDTO) {
        ReservationResponseDTO createdReservation = reservationService.create(reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PatchMapping("/{id}/checkin")
    @Operation(
            summary = "Realizar check-in",
            description = "Altera o status da reserva para CHECKED_IN, permitindo apenas de reservas no status CREATED"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Check-in realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito - Status inválido para check-in",
                    content = @Content
            )
    })
    public ResponseEntity<ReservationResponseDTO> checkIn(
            @Parameter(description = "ID único da reserva", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id) {
        ReservationResponseDTO updatedReservation = reservationService.checkIn(id);
        return ResponseEntity.ok(updatedReservation);
    }

    @PatchMapping("/{id}/checkout")
    @Operation(
            summary = "Realizar check-out",
            description = "Altera o status da reserva para CHECKED_OUT e calcula o valor total baseado nas diárias"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Check-out realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito - Status inválido para check-out",
                    content = @Content
            )
    })
    public ResponseEntity<ReservationResponseDTO> checkOut(
            @Parameter(description = "ID único da reserva", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id) {
        ReservationResponseDTO updatedReservation = reservationService.checkOut(id);
        return ResponseEntity.ok(updatedReservation);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(
            summary = "Cancelar reserva",
            description = "Altera o status da reserva para CANCELED, permitindo apenas de reservas no status CREATED"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva cancelada com sucesso",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito - Status inválido para cancelamento",
                    content = @Content
            )
    })
    public ResponseEntity<ReservationResponseDTO> cancel(
            @Parameter(description = "ID único da reserva", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String id) {
        ReservationResponseDTO updatedReservation = reservationService.cancel(id);
        return ResponseEntity.ok(updatedReservation);
    }
}