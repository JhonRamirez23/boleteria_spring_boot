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

@ExtendWith(MockitoExtension.class)
@DisplayName("Validación de propietario de reserva")
class ValidarPropietarioTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private FuncionService funcionService;

    @InjectMocks
    private ReservaValidacionService validacionService;

    @Test
    @DisplayName("Debe lanzar BusinessException si el usuario no es el propietario")
    void cuandoUsuarioNoPropietario_lanzaBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarPropietario(
                        "otro@codesa.com", "user@codesa.com"));

        assertEquals("No puedes cancelar una reserva que no es tuya", ex.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción si el usuario es el propietario")
    void cuandoUsuarioEsPropietario_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validacionService.validarPropietario(
                "user@codesa.com", "user@codesa.com"));
    }

    @Test
    @DisplayName("Debe lanzar BusinessException si el email es diferente en mayúsculas")
    void cuandoEmailDiferenciaEnMayusculas_lanzaBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> validacionService.validarPropietario(
                        "user@codesa.com", "USER@codesa.com"));

        assertEquals("No puedes cancelar una reserva que no es tuya", ex.getMessage());
    }
}
