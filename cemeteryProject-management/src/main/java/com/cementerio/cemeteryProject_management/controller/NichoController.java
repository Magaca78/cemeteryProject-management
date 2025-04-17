package com.cementerio.cemeteryProject_management.controller;

import com.cementerio.cemeteryProject_management.dto.NichoDTO;
import com.cementerio.cemeteryProject_management.service.NichoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nichos")
public class NichoController {

    private final NichoService nichoService;

    public NichoController(NichoService nichoService) {
        this.nichoService = nichoService;
    }

    @GetMapping
    public List<NichoDTO> listarTodos() {
        return nichoService.getAll();
    }

    @GetMapping("/{codigo}")
    public NichoDTO obtenerPorCodigo(@PathVariable String codigo) {
        return nichoService.getById(codigo);
    }

    @PostMapping
    public NichoDTO crear(@RequestBody NichoDTO dto) {
        return nichoService.save(dto);
    }

    @PutMapping("/{codigo}")
    public NichoDTO actualizar(@PathVariable String codigo, @RequestBody NichoDTO dto) {
        return nichoService.update(codigo, dto);
    }

    @DeleteMapping("/{codigo}")
    public void eliminar(@PathVariable String codigo) {
        nichoService.delete(codigo);
    }
}

