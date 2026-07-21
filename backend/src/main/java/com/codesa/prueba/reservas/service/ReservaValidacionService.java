package com.codesa.prueba.reservas.service;

import com.codesa.prueba.funciones.service.FuncionService;
import com.codesa.prueba.reservas.model.EstadoReserva;
import com.codesa.prueba.reservas.repository.ReservaRepository;
import com.codesa.prueba.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaValidacionService {

    private final ReservaRepository reservaRepository;
    private final FuncionService funcionService;

    public void validarReservaduplicada(Long usuarioId, Long funcionId) {
        if (reservaRepository.existsByUsuarioIdAndFuncionIdAndEstadoIn(
                usuarioId, funcionId,
                List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA))) {
            throw new BusinessException(
                    "Ya tienes una reserva activa para esta función");
        }
    }

    public void validarAforo(Long funcionId, Integer cantidad) {
        Integer aforoDisponible = funcionService.calcularAforoDisponible(funcionId);
        log.info("Aforo disponible para función {}: {}", funcionId, aforoDisponible);
        if (cantidad > aforoDisponible) {
            throw new BusinessException(
                    "No hay suficiente aforo disponible. Disponible: " + aforoDisponible);
        }
    }

    public void validarTransicionEstado(EstadoReserva estadoActual,
                                         EstadoReserva estadoRequerido,
                                         String mensaje) {
        if (estadoActual != estadoRequerido) {
            throw new BusinessException(mensaje);
        }
    }

    public void validarPropietario(String emailReserva, String emailSolicitante) {
        if (!emailReserva.equals(emailSolicitante)) {
            throw new BusinessException(
                    "No puedes cancelar una reserva que no es tuya");
        }
    }
}
