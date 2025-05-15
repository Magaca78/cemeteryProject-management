package com.cementerio.cemeteryProject_management.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

import java.util.UUID;

@Entity
@Table(name = "eventocuerpo")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class EventoCuerpoModel {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cadaver", nullable = false)
    private CuerpoInhumadoModel cuerpoInhumado;

    @NotNull
    private LocalDate fechaEvento;

    @NotNull
    private String tipoEvento;

    @NotNull
    private String resumenEvento;

    private String archivo;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
