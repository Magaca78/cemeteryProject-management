package com.cementerio.cemeteryProject_management.dtos;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel.EstadoCuerpo;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CuerpoInhumadoSTRDTO {
    private String idCadaver;
    private String nombre;
    private String apellido;
    private String documentoIdentidad;
    private String numeroProtocoloNecropsia;
    private String causaMuerte;
    private LocalDate fechaNacimiento;
    private LocalDate fechaDefuncion;
    private LocalDate fechaIngreso;
    private LocalDate fechaInhumacion;
    private LocalDate fechaExhumacion = null; // Puede ser null si no se ha exhumado
    private String funcionarioReceptor;
    private String cargoFuncionario;
    private String autoridadRemitente;
    private String cargoAutoridadRemitente;
    private String autoridadExhumacion;
    private String cargoAutoridadExhumacion;
    private EstadoCuerpo estado;
    private String observaciones;
}