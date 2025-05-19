package com.cementerio.cemeteryProject_management.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data   
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoCuerpoDTOShow {
    private String id;
    private String idCadaver;
    private LocalDate fechaEvento;
    private String tipoEvento;
    private String resumenEvento;
    private String archivo;
  
}
