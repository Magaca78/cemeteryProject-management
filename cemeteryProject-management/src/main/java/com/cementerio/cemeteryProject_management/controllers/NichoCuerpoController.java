package com.cementerio.cemeteryProject_management.controllers;

import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.dtos.NichoCuerpoDTO;
import com.cementerio.cemeteryProject_management.services.NichoCuerpoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nichoscuerpos")
public class NichoCuerpoController {

    @Autowired
    private NichoCuerpoService service;

    @GetMapping
    public List<NichoCuerpoDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NichoCuerpoDTO> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NichoCuerpoDTO create(@RequestBody NichoCuerpoDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NichoCuerpoDTO> update(@PathVariable String id, @RequestBody NichoCuerpoDTO dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nicho/{codigoNicho}")
    public ResponseEntity<CuerpoInhumadoDTO> getCuerpoByCodigoNicho(@PathVariable String codigoNicho) {
        return service.getCuerpoByCodigoNicho(codigoNicho)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nichoByID/{codigoNicho}")
    public ResponseEntity<NichoCuerpoDTO> getByCodigoNicho(@PathVariable String codigoNicho) {
        return service.getByCodigoNicho(codigoNicho)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
