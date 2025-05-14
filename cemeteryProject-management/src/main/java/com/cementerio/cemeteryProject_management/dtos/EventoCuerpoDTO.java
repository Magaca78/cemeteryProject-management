package com.cementerio.cemeteryProject_management.dtos;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoCuerpoDTO {
    private String id;
    private String idCadaver;
    private LocalDate fechaEvento;
    private String tipoEvento;
    private String resumenEvento;
    private String archivo;
}
