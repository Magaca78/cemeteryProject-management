package com.cementerio.cemeteryProject_management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CuerpoInhumadoDTO {
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
    private String estado; // Enum como String
    private String observaciones;
}

