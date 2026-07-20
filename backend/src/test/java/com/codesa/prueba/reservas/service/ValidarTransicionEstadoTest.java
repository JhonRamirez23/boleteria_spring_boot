package com.codesa.prueba.reservas.service;

import com.codesa.prueba.funciones.service.FuncionService;
import com.codesa.prueba.reservas.model.EstadoReserva;
import com.codesa.prueba.reservas.repository.ReservaRepository;
import com.codesa.prueba.shared.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Validación de transición de estado de reserva")
class ValidarTransicionEstadoTest {
    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private FuncionService funcionService;

    @InjectMocks
    private ReservaValidacionService validacionService;

    @Test
    @DisplayName("Debe lanzar BusinessException al confirmar una reserva ya CONFIRMADA")
    void cuandoReservaYaConfirmada_lanzaBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarTransicionEstado(
                        EstadoReserva.CONFIRMADA,
                        EstadoReserva.PENDIENTE,
                        "Solo se pueden confirmar reservas en estado PENDIENTE"));

        assertEquals("Solo se pueden confirmar reservas en estado PENDIENTE",
                ex.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException al confirmar una reserva RECHAZADA")
    void cuandoReservaRechazada_lanzaBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarTransicionEstado(
                        EstadoReserva.RECHAZADA,
                        EstadoReserva.PENDIENTE,
                        "Solo se pueden confirmar reservas en estado PENDIENTE"));

        assertEquals("Solo se pueden confirmar reservas en estado PENDIENTE",
                ex.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException al confirmar una reserva CANCELADA")
    void cuandoReservaCancelada_lanzaBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarTransicionEstado(
                        EstadoReserva.CANCELADA,
                        EstadoReserva.PENDIENTE,
                        "Solo se pueden confirmar reservas en estado PENDIENTE"));

        assertEquals("Solo se pueden confirmar reservas en estado PENDIENTE",
                ex.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción al confirmar una reserva PENDIENTE")
    void cuandoReservaPendiente_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validacionService.validarTransicionEstado(
                EstadoReserva.PENDIENTE,
                EstadoReserva.PENDIENTE,
                "Solo se pueden confirmar reservas en estado PENDIENTE"));
    }
}
