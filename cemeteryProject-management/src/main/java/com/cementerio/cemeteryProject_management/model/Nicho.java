package com.cementerio.cemeteryProject_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nicho {

    @Id
    private String codigo;
    
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    private EstadoNicho estado;

    public enum EstadoNicho {
        DISPONIBLE,
        OCUPADO,
        MANTENIMIENTO
    }
}
