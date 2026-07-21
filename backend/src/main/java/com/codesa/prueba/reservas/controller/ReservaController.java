package com.codesa.prueba.reservas.controller;

import com.codesa.prueba.reservas.dto.ReservaRequest;
import com.codesa.prueba.reservas.dto.ReservaResponse;
import com.codesa.prueba.reservas.model.EstadoReserva;
import com.codesa.prueba.reservas.service.ReservaEstadoService;
import com.codesa.prueba.reservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaEstadoService reservaEstadoService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservaResponse> crear(
            @Valid @RequestBody ReservaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Solicitud de reserva de usuario: {}", userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaService.crear(request, userDetails.getUsername()));
    }

    @GetMapping("/mis-reservas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ReservaResponse>> listarMisReservas(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(
                reservaService.listarMisReservas(userDetails.getUsername(), pageable));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservaResponse>> listarTodas(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reservaService.listarTodas(pageable));
    }

    @GetMapping("/funcion/{funcionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservaResponse>> listarPorFuncion(
            @PathVariable Long funcionId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reservaService.listarPorFuncion(funcionId, pageable));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservaResponse>> listarPorEstado(
            @PathVariable EstadoReserva estado,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reservaService.listarPorEstado(estado, pageable));
    }

    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservaResponse> confirmar(@PathVariable Long id) {
        log.info("Solicitud de confirmación de reserva id: {}", id);
        return ResponseEntity.ok(reservaEstadoService.confirmar(id));
    }

    @PatchMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservaResponse> rechazar(@PathVariable Long id) {
        log.info("Solicitud de rechazo de reserva id: {}", id);
        return ResponseEntity.ok(reservaEstadoService.rechazar(id));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservaResponse> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Solicitud de cancelación de reserva id: {} por: {}",
                id, userDetails.getUsername());
        return ResponseEntity.ok(
                reservaEstadoService.cancelar(id, userDetails.getUsername()));
    }
}
