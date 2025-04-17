package com.cementerio.cemeteryProject_management.controller;

import com.cementerio.cemeteryProject_management.dto.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.service.CuerpoInhumadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuerpos")
public class CuerpoInhumadoController {

    private final CuerpoInhumadoService service;

    public CuerpoInhumadoController(CuerpoInhumadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CuerpoInhumadoDTO> listarTodos() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CuerpoInhumadoDTO obtenerPorId(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public CuerpoInhumadoDTO guardar(@RequestBody CuerpoInhumadoDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public CuerpoInhumadoDTO actualizar(@PathVariable String id, @RequestBody CuerpoInhumadoDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.delete(id);
    }
}

