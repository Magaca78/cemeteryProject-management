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

    @PostMapping
    public void guardar(@RequestBody CuerpoInhumadoDTO dto) {
        service.save(dto);
    }
}
