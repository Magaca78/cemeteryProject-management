package com.cementerio.cemeteryProject_management.controllers;

import com.cementerio.cemeteryProject_management.dtos.EventoCuerpoDTOCreate;
import com.cementerio.cemeteryProject_management.dtos.EventoCuerpoDTOShow;
import com.cementerio.cemeteryProject_management.services.EventoCuerpoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/eventoscuerpos")
public class EventoCuerpoController {

    @Autowired
    private EventoCuerpoService service;

    @GetMapping
    public List<EventoCuerpoDTOShow> getAll() {
        return service.getAllEventosShow();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoCuerpoDTOShow> getById(@PathVariable String id) {
        return service.getEventoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> create(
            @RequestPart("idCadaver") String idCadaver,
            @RequestPart("fechaEvento") String fechaEvento,
            @RequestPart("tipoEvento") String tipoEvento,
            @RequestPart("resumenEvento") String resumenEvento,
            @RequestPart("archivo") MultipartFile archivo) {
        try {
            // Crear un DTO para pasar los datos al servicio
            EventoCuerpoDTOCreate dto = new EventoCuerpoDTOCreate();
            dto.setIdCadaver(idCadaver);
            dto.setFechaEvento(LocalDate.parse(fechaEvento)); // Convertir String a LocalDate
            dto.setTipoEvento(tipoEvento);
            dto.setResumenEvento(resumenEvento);
            dto.setArchivo(archivo);

            // Llamar al servicio para crear el evento
            EventoCuerpoDTOCreate created = service.createEvento(dto);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoCuerpoDTOCreate> update(@PathVariable String id, @RequestBody EventoCuerpoDTOCreate dto) {
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
    public List<EventoCuerpoDTOShow> getEventosPorCuerpo(@PathVariable String idCadaver) {
        return service.getEventosPorCuerpo(idCadaver);
    }

    @GetMapping("/ultimos")
    public List<EventoCuerpoDTOShow> getUltimosEventos(@RequestParam(defaultValue = "10") int cantidad) {
        return service.getLatestEventos(cantidad);
    }

    @GetMapping("/search")
    public List<EventoCuerpoDTOShow> searchEventos(@RequestParam String query) {
        return service.searchEventos(query);
    }

    /**
     * Obtener eventos por tipo y rango de fechas
     * Ejemplo: /eventoscuerpos/filtrar?tipo=Inhumacion&desde=2024-01-01&hasta=2025-01-01
     */
    @GetMapping("/filtrar")
    public List<EventoCuerpoDTOShow> getEventosPorTipoYRango(
            @RequestParam String tipo,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta
    ) {
        LocalDate fechaDesde = (desde != null && !desde.isEmpty()) ? LocalDate.parse(desde) : null;
        LocalDate fechaHasta = (hasta != null && !hasta.isEmpty()) ? LocalDate.parse(hasta) : null;
        return service.getEventosPorTipoYRango(tipo, fechaDesde, fechaHasta);
    }
}
