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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Validacion de reserva duplicada")
class ValidarReservaDuplicadaTest {
    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private FuncionService funcionService;

    @InjectMocks
    private ReservaValidacionService validacionService;

    @Test
    @DisplayName("Debe lanzar BusinessException si el usuario ya tiene reserva activa")
    void cuandoExisteReservaActiva_lanzaBusinessException() {
        when(reservaRepository.existsByUsuarioIdAndFuncionIdAndEstadoIn(
                1L, 1L, List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA)))
                .thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarReservaduplicada(1L, 1L));

        assertEquals("Ya tienes una reserva activa para esta función", ex.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción si el usuario no tiene reserva activa")
    void cuandoNoExisteReservaActiva_noLanzaExcepcion() {
        when(reservaRepository.existsByUsuarioIdAndFuncionIdAndEstadoIn(
                1L, 1L, List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA)))
                .thenReturn(false);

        assertDoesNotThrow(() -> validacionService.validarReservaduplicada(1L, 1L));
    }
}
