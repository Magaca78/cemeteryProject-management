package com.cementerio.cemeteryProject_management.controller;

import com.cementerio.cemeteryProject_management.dto.NichoCuerpoDTO;
import com.cementerio.cemeteryProject_management.service.NichoCuerpoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nichos-cuerpo")
public class NichoCuerpoController {

    private final NichoCuerpoService service;

    public NichoCuerpoController(NichoCuerpoService service) {
        this.service = service;
    }

    @GetMapping
    public List<NichoCuerpoDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public NichoCuerpoDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public NichoCuerpoDTO create(@RequestBody NichoCuerpoDTO dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
