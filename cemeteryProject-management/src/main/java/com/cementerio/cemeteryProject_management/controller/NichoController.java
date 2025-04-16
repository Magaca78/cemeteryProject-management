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

    @GetMapping("/{id}")
    public NichoDTO obtenerPorId(@PathVariable Long id) {
        return nichoService.getById(id);
    }

    @PostMapping
    public NichoDTO crear(@RequestBody NichoDTO dto) {
        return nichoService.save(dto);
    }

    @PutMapping("/{id}")
    public NichoDTO actualizar(@PathVariable Long id, @RequestBody NichoDTO dto) {
        return nichoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        nichoService.delete(id);
    }
}
