package com.codesa.prueba.obras.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObraResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private String genero;
    private Integer duracionMinutos;
    private Boolean activa;
}
