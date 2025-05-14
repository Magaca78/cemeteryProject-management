package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.EventoCuerpoDTO;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;
import com.cementerio.cemeteryProject_management.models.EventoCuerpoModel;
import com.cementerio.cemeteryProject_management.repositories.ICuerpoInhumadoRepository;
import com.cementerio.cemeteryProject_management.repositories.IEventoCuerpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventoCuerpoService {

    @Autowired
    private IEventoCuerpoRepository eventoRepository;

    @Autowired
    private ICuerpoInhumadoRepository cuerpoRepository;

    /* =========================
       CRUD BÁSICO
       ========================= */

    public List<EventoCuerpoDTO> getAllEventos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventoCuerpoDTO> getEventoById(String id) {
        return eventoRepository.findById(id).map(this::convertToDTO);
    }

    public EventoCuerpoDTO createEvento(EventoCuerpoDTO dto) {
        // Validar existencia del cuerpo
        CuerpoInhumadoModel cuerpo = cuerpoRepository.findById(dto.getIdCadaver())
                .orElseThrow(() -> new IllegalArgumentException("El cuerpo no existe"));

        EventoCuerpoModel model = convertToModel(dto);
        model.setId(null);               // Dejar que @PrePersist genere el UUID
        model.setCuerpoInhumado(cuerpo);

        return convertToDTO(eventoRepository.save(model));
    }

    public Optional<EventoCuerpoDTO> updateEvento(String id, EventoCuerpoDTO dto) {
        return eventoRepository.findById(id).map(existing -> {
            // Si cambia el cadáver, validar el nuevo
            if (!existing.getCuerpoInhumado().getIdCadaver().equals(dto.getIdCadaver())) {
                CuerpoInhumadoModel nuevoCuerpo = cuerpoRepository.findById(dto.getIdCadaver())
                        .orElseThrow(() -> new IllegalArgumentException("El cuerpo no existe"));
                existing.setCuerpoInhumado(nuevoCuerpo);
            }
            existing.setFechaEvento(dto.getFechaEvento());
            existing.setTipoEvento(dto.getTipoEvento());
            existing.setResumenEvento(dto.getResumenEvento());
            existing.setArchivo(dto.getArchivo());
            return convertToDTO(eventoRepository.save(existing));
        });
    }

    public boolean deleteEvento(String id) {
        if (eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /* =========================
       MÉTODOS ESPECÍFICOS
       ========================= */

    /** Devuelve todos los eventos de un cadáver ordenados por fecha descendente */
    public List<EventoCuerpoDTO> getEventosPorCuerpo(String idCadaver) {
        return eventoRepository.findByCuerpoInhumado_IdCadaver(idCadaver)
                .stream()
                .sorted((a, b) -> b.getFechaEvento().compareTo(a.getFechaEvento()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /** Devuelve los últimos N eventos globales por fechaEvento */
    public List<EventoCuerpoDTO> getLatestEventos(int cantidad) {
        Pageable pageable = PageRequest.of(0, cantidad);
        return eventoRepository.findLatestEventos(pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /** Busca texto libre en el resumen del evento */
    public List<EventoCuerpoDTO> searchEventos(String texto) {
        return eventoRepository.searchByResumen(texto)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /** Filtra por tipo de evento (IgnoreCase) y, opcionalmente, por rango de fechas */
    public List<EventoCuerpoDTO> getEventosPorTipoYRango(
            String tipoEvento,
            LocalDate desde,
            LocalDate hasta) {

        List<EventoCuerpoModel> base = eventoRepository.findByTipoEventoIgnoreCase(tipoEvento);

        return base.stream()
                .filter(e -> (desde == null || !e.getFechaEvento().isBefore(desde)) &&
                             (hasta == null || !e.getFechaEvento().isAfter(hasta)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /* =========================
       CONVERSIÓN DTO ↔ MODEL
       ========================= */

    private EventoCuerpoDTO convertToDTO(EventoCuerpoModel model) {
        EventoCuerpoDTO dto = new EventoCuerpoDTO();
        dto.setId(model.getId());
        dto.setIdCadaver(model.getCuerpoInhumado().getIdCadaver());
        dto.setFechaEvento(model.getFechaEvento());
        dto.setTipoEvento(model.getTipoEvento());
        dto.setResumenEvento(model.getResumenEvento());
        dto.setArchivo(model.getArchivo());
        return dto;
    }

    private EventoCuerpoModel convertToModel(EventoCuerpoDTO dto) {
        EventoCuerpoModel model = new EventoCuerpoModel();
        // Cuerpo se asigna arriba (create/update)
        model.setFechaEvento(dto.getFechaEvento());
        model.setTipoEvento(dto.getTipoEvento());
        model.setResumenEvento(dto.getResumenEvento());
        model.setArchivo(dto.getArchivo());
        return model;
    }
}
