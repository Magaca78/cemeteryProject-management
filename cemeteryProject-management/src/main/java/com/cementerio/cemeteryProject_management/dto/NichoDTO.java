package com.cementerio.cemeteryProject_management.dto;

import lombok.Data;

@Data
public class NichoDTO {
    private String codigo;
    private String ubicacion;
    private String estado; // DISPONIBLE, OCUPADO, MANTENIMIENTO
}
