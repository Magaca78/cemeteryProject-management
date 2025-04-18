package com.cementerio.cemeteryProject_management.dtos;

import com.cementerio.cemeteryProject_management.models.NichoModel.EstadoNicho;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Esto asegura que el campo código solo se incluya si es no nulo
public class NichoDTO {
    private String codigo; // El código será generado automáticamente, pero puedes mantenerlo en el DTO
    private String ubicacion;
    private EstadoNicho estado;
}
