package com.codesa.prueba.reservas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.codesa.prueba.reservas.model.EstadoReserva;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservaResponse {
    private Long id;
    private Long funcionId;
    private String obraTitulo;
    private String sala;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fechaFuncion;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;
    private EstadoReserva estado;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fechaReserva;

    private String usuarioEmail;
}
