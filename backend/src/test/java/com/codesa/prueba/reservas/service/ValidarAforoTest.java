package com.codesa.prueba.reservas.service;

import com.codesa.prueba.funciones.service.FuncionService;
import com.codesa.prueba.reservas.repository.ReservaRepository;
import com.codesa.prueba.shared.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Validación de aforo disponible")
class ValidarAforoTest {
    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private FuncionService funcionService;

    @InjectMocks
    private ReservaValidacionService validacionService;

    @Test
    @DisplayName("Debe lanzar BusinessException si no hay aforo suficiente")
    void cuandoAforoInsuficiente_lanzaBusinessException() {
        when(funcionService.calcularAforoDisponible(1L)).thenReturn(5);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarAforo(1L, 10));

        assertTrue(ex.getMessage().contains("No hay suficiente aforo disponible"));
    }

    @Test
    @DisplayName("No debe lanzar excepción si hay aforo suficiente")
    void cuandoAforoSuficiente_noLanzaExcepcion() {
        when(funcionService.calcularAforoDisponible(1L)).thenReturn(50);

        assertDoesNotThrow(() -> validacionService.validarAforo(1L, 10));
    }

    @Test
    @DisplayName("Debe lanzar BusinessException si el aforo disponible es exactamente menor a la cantidad")
    void cuandoCantidadIgualAlAforo_noLanzaExcepcion() {
        when(funcionService.calcularAforoDisponible(1L)).thenReturn(10);

        assertDoesNotThrow(() -> validacionService.validarAforo(1L, 10));
    }

    @Test
    @DisplayName("Debe lanzar BusinessException si el aforo disponible es cero")
    void cuandoAforoEsCero_lanzaBusinessException() {
        when(funcionService.calcularAforoDisponible(1L)).thenReturn(0);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarAforo(1L, 1));

        assertTrue(ex.getMessage().contains("No hay suficiente aforo disponible"));
    }
}