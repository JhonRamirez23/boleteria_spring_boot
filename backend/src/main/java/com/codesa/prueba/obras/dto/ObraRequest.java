package com.codesa.prueba.obras.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ObraRequest {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede tener más de 200 caracteres")
    private String titulo;

    private String descripcion;

    @Size(max = 100, message = "El género no puede tener más de 100 caracteres")
    private String genero;

    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
    private Integer duracionMinutos;

    private Boolean activa = true;
}
