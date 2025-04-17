package com.cementerio.cemeteryProject_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NichoCuerpo {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_cadaver", nullable = false)
    private CuerpoInhumado cuerpoInhumado;

    @ManyToOne
    @JoinColumn(name = "codigo_nicho", referencedColumnName = "codigo", nullable = false)
    private Nicho nicho;
}
