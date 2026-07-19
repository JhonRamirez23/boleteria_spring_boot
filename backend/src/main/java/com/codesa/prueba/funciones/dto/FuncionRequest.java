package com.codesa.prueba.funciones.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FuncionRequest {
    @NotNull(message = "El id de la obra es obligatorio")
    private Long obraId;

    @NotNull(message = "La fecha y hora es obligatoria")
    @Future(message = "La fecha y hora debe ser futura")
    private LocalDateTime fechaHora;

    @NotBlank(message = "La sala es obligatoria")
    private String sala;

    @NotNull(message = "El aforo total es obligatorio")
    @Min(value = 1, message = "El aforo debe ser mayor a 0")
    private Integer aforoTotal;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
}
