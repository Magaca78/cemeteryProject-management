package com.cementerio.cemeteryProject_management.controllers;

import com.cementerio.cemeteryProject_management.dtos.NichoDTO;
import com.cementerio.cemeteryProject_management.services.NichoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nichos")
public class NichoController {

    @Autowired
    private NichoService nichoService;

    @GetMapping
    public List<NichoDTO> getAll() {
        return nichoService.getAllNichos();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<NichoDTO> getById(@PathVariable String codigo) {
        return nichoService.getNichoById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NichoDTO create(@RequestBody NichoDTO dto) {
        return nichoService.createNicho(dto);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<NichoDTO> update(@PathVariable String codigo, @RequestBody NichoDTO dto) {
        return nichoService.updateNicho(codigo, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable String codigo) {
        if (nichoService.deleteNicho(codigo)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
