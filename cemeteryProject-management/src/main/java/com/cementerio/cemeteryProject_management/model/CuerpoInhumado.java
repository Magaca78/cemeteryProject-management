package com.cementerio.cemeteryProject_management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class CuerpoInhumado {

    @Id
    private String idCadaver;

    private String nombre;
    private String apellido;
    private String documentoIdentidad;
    private String numeroProtocoloNecropsia;
    private String causaMuerte;
    private LocalDate fechaNacimiento;
    private LocalDate fechaDefuncion;
    private LocalDateTime fechaIngreso;
    private LocalDate fechaInhumacion;
    private LocalDate fechaExhumacion;

    private String funcionarioReceptor;
    private String cargoFuncionario;
    private String autoridadRemitente;
    private String cargoAutoridadRemitente;
    private String autoridadExhumacion;
    private String cargoAutoridadExhumacion;

    @Enumerated(EnumType.STRING)
    private EstadoCuerpo estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    public enum EstadoCuerpo {
        Inhumado,
        Exhumado
    }
}
