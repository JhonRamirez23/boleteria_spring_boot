package com.codesa.prueba.reservas.service;

import com.codesa.prueba.reservas.dto.ReservaResponse;
import com.codesa.prueba.reservas.model.EstadoReserva;
import com.codesa.prueba.reservas.model.Reserva;
import com.codesa.prueba.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaEstadoService {

    private final ReservaRepository reservaRepository;
    private final ReservaService reservaService;
    private final ReservaValidacionService validacionService;

    @Transactional
    public ReservaResponse confirmar(Long id) {
        log.info("Confirmando reserva id: {}", id);
        Reserva reserva = reservaService.findReservaOrThrow(id);
        validacionService.validarTransicionEstado(
                reserva.getEstado(), EstadoReserva.PENDIENTE,
                "Solo se pueden confirmar reservas en estado PENDIENTE");
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        return reservaService.toResponse(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaResponse rechazar(Long id) {
        log.info("Rechazando reserva id: {}", id);
        Reserva reserva = reservaService.findReservaOrThrow(id);
        validacionService.validarTransicionEstado(
                reserva.getEstado(), EstadoReserva.PENDIENTE,
                "Solo se pueden rechazar reservas en estado PENDIENTE");
        reserva.setEstado(EstadoReserva.RECHAZADA);
        return reservaService.toResponse(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaResponse cancelar(Long id, String email) {
        log.info("Cancelando reserva id: {} por usuario: {}", id, email);
        Reserva reserva = reservaService.findReservaOrThrow(id);
        validacionService.validarPropietario(
                reserva.getUsuario().getEmail(), email);
        validacionService.validarTransicionEstado(
                reserva.getEstado(), EstadoReserva.PENDIENTE,
                "Solo se pueden cancelar reservas en estado PENDIENTE");
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaService.toResponse(reservaRepository.save(reserva));
    }
}
