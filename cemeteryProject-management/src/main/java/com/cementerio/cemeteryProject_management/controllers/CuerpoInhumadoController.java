package com.cementerio.cemeteryProject_management.controllers;

import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.services.CuerpoInhumadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuerposinhumados")
public class CuerpoInhumadoController {

    @Autowired
    private CuerpoInhumadoService cuerpoInhumadoService;

    @GetMapping
    public List<CuerpoInhumadoDTO> getAll() {
        return cuerpoInhumadoService.getAllCuerposInhumados();
    }

    @GetMapping("/{idCadaver}")
    public ResponseEntity<CuerpoInhumadoDTO> getById(@PathVariable String idCadaver) {
        return cuerpoInhumadoService.getCuerpoInhumadoById(idCadaver)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CuerpoInhumadoDTO create(@RequestBody CuerpoInhumadoDTO dto) {
        return cuerpoInhumadoService.createCuerpoInhumado(dto);
    }

    @PutMapping("/{idCadaver}")
    public ResponseEntity<CuerpoInhumadoDTO> update(@PathVariable String idCadaver, @RequestBody CuerpoInhumadoDTO dto) {
        return cuerpoInhumadoService.updateCuerpoInhumado(idCadaver, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idCadaver}")
    public ResponseEntity<Void> delete(@PathVariable String idCadaver) {
        if (cuerpoInhumadoService.deleteCuerpoInhumado(idCadaver)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}