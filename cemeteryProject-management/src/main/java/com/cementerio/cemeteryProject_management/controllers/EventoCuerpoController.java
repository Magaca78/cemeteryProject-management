package com.cementerio.cemeteryProject_management.controllers;

import com.cementerio.cemeteryProject_management.dtos.EventoCuerpoDTO;
import com.cementerio.cemeteryProject_management.services.EventoCuerpoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/eventoscuerpos")
public class EventoCuerpoController {

    @Autowired
    private EventoCuerpoService service;

    @GetMapping
    public List<EventoCuerpoDTO> getAll() {
        return service.getAllEventos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoCuerpoDTO> getById(@PathVariable String id) {
        return service.getEventoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EventoCuerpoDTO create(@RequestBody EventoCuerpoDTO dto) {
        return service.createEvento(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoCuerpoDTO> update(@PathVariable String id, @RequestBody EventoCuerpoDTO dto) {
        return service.updateEvento(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.deleteEvento(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cuerpo/{idCadaver}")
    public List<EventoCuerpoDTO> getEventosPorCuerpo(@PathVariable String idCadaver) {
        return service.getEventosPorCuerpo(idCadaver);
    }

    @GetMapping("/ultimos")
    public List<EventoCuerpoDTO> getUltimosEventos(@RequestParam(defaultValue = "10") int cantidad) {
        return service.getLatestEventos(cantidad);
    }

    @GetMapping("/search")
    public List<EventoCuerpoDTO> searchEventos(@RequestParam String query) {
        return service.searchEventos(query);
    }

    /**
     * Obtener eventos por tipo y rango de fechas
     * Ejemplo: /eventoscuerpos/filtrar?tipo=Inhumacion&desde=2024-01-01&hasta=2025-01-01
     */
    @GetMapping("/filtrar")
    public List<EventoCuerpoDTO> getEventosPorTipoYRango(
            @RequestParam String tipo,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta
    ) {
        LocalDate fechaDesde = (desde != null && !desde.isEmpty()) ? LocalDate.parse(desde) : null;
        LocalDate fechaHasta = (hasta != null && !hasta.isEmpty()) ? LocalDate.parse(hasta) : null;
        return service.getEventosPorTipoYRango(tipo, fechaDesde, fechaHasta);
    }
}
