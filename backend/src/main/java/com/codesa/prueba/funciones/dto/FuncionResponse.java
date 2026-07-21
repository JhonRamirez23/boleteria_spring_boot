package com.codesa.prueba.funciones.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FuncionResponse {
    private Long id;
    private Long obraId;
    private String obraTitulo;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fechaHora;

    private String sala;
    private Integer aforoTotal;
    private Integer aforoDisponible;
    private BigDecimal precio;
}
