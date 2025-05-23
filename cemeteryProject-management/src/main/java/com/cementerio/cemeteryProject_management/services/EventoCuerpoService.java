package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.EventoCuerpoDTOCreate;
import com.cementerio.cemeteryProject_management.dtos.EventoCuerpoDTOShow;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;
import com.cementerio.cemeteryProject_management.models.EventoCuerpoModel;
import com.cementerio.cemeteryProject_management.repositories.ICuerpoInhumadoRepository;
import com.cementerio.cemeteryProject_management.repositories.IEventoCuerpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    private CloudinaryService cloudinaryService; // Inyectar CloudinaryService

    /* =========================
       CRUD BÁSICO
       ========================= */

    public List<EventoCuerpoDTOShow> getAllEventosShow() {
        return eventoRepository.findAll()
                .stream()
                .map(this::convertToDTOShow)
                .collect(Collectors.toList());
    }

    public Optional<EventoCuerpoDTOShow> getEventoById(String id) {
        return eventoRepository.findById(id).map(this::convertToDTOShow);
    }

    public EventoCuerpoDTOCreate createEvento(EventoCuerpoDTOCreate dto) throws IOException {
        // Validar existencia del cuerpo
        CuerpoInhumadoModel cuerpo = cuerpoRepository.findById(dto.getIdCadaver())
                .orElseThrow(() -> new IllegalArgumentException("El cuerpo no existe"));

        // Subir el archivo a Cloudinary y obtener la URL
        String urlArchivo = null;
        if (dto.getArchivo() != null && !dto.getArchivo().isEmpty()) {
            urlArchivo = cloudinaryService.uploadFile(dto.getArchivo());
        } else {
            throw new IllegalArgumentException("El archivo es obligatorio");
        }

        // Crear el modelo y asignar la URL
        EventoCuerpoModel model = convertToModel(dto);
        model.setId(null); // Dejar que @PrePersist genere el UUID
        model.setCuerpoInhumado(cuerpo);
        model.setArchivo(urlArchivo); // Asignar la URL al modelo

        // Guardar en la base de datos
        return convertToDTO(eventoRepository.save(model));
    }

    public Optional<EventoCuerpoDTOCreate> updateEvento(String id, EventoCuerpoDTOCreate dto) {
        return eventoRepository.findById(id).map(existing -> {
            // Si cambia el cadáver, validar el nuevo
            if (!existing.getCuerpoInhumado().getIdCadaver().equals(dto.getIdCadaver())) {
                CuerpoInhumadoModel nuevoCuerpo = cuerpoRepository.findById(dto.getIdCadaver())
                        .orElseThrow(() -> new IllegalArgumentException("El cuerpo no existe"));
                existing.setCuerpoInhumado(nuevoCuerpo);
            }
            existing.setFechaEvento(dto.getFechaEvento());
            existing.setTipoEvento(dto.getTipoEvento());
            existing.setResumenEvento(dto.getResumenEvento());
            // Nota: Si deseas permitir actualizar el archivo, aquí también deberías manejar la subida a Cloudinary
            existing.setArchivo(dto.getArchivo() != null ? dto.getArchivo().toString() : existing.getArchivo());
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

    public List<EventoCuerpoDTOShow> getEventosPorCuerpo(String idCadaver) {
        return eventoRepository.findByCuerpoInhumado_IdCadaver(idCadaver)
                .stream()
                .sorted((a, b) -> b.getFechaEvento().compareTo(a.getFechaEvento()))
                .map(this::convertToDTOShow)
                .collect(Collectors.toList());
    }

    public List<EventoCuerpoDTOShow> getLatestEventos(int cantidad) {
        Pageable pageable = PageRequest.of(0, cantidad);
        return eventoRepository.findLatestEventos(pageable)
                .stream()
                .map(this::convertToDTOShow)
                .collect(Collectors.toList());
    }

    public List<EventoCuerpoDTOShow> searchEventos(String texto) {
        return eventoRepository.searchByResumen(texto)
                .stream()
                .map(this::convertToDTOShow)
                .collect(Collectors.toList());
    }

    public List<EventoCuerpoDTOShow> getEventosPorTipoYRango(
            String tipoEvento,
            LocalDate desde,
            LocalDate hasta) {

        List<EventoCuerpoModel> base = eventoRepository.findByTipoEventoIgnoreCase(tipoEvento);

        return base.stream()
                .filter(e -> (desde == null || !e.getFechaEvento().isBefore(desde)) &&
                        (hasta == null || !e.getFechaEvento().isAfter(hasta)))
                .map(this::convertToDTOShow)
                .collect(Collectors.toList());
    }

    /* =========================
       CONVERSIÓN DTO ↔ MODEL
       ========================= */

    private EventoCuerpoDTOCreate convertToDTO(EventoCuerpoModel model) {
        EventoCuerpoDTOCreate dto = new EventoCuerpoDTOCreate();
        dto.setId(model.getId());
        dto.setIdCadaver(model.getCuerpoInhumado().getIdCadaver());
        dto.setFechaEvento(model.getFechaEvento());
        dto.setTipoEvento(model.getTipoEvento());
        dto.setResumenEvento(model.getResumenEvento());
        dto.setArchivo(null); // No devolvemos el MultipartFile, solo la URL
        return dto;
    }

    private EventoCuerpoDTOShow convertToDTOShow(EventoCuerpoModel model) {
        EventoCuerpoDTOShow dto = new EventoCuerpoDTOShow();
        dto.setId(model.getId());
        dto.setIdCadaver(model.getCuerpoInhumado().getIdCadaver());
        dto.setFechaEvento(model.getFechaEvento());
        dto.setTipoEvento(model.getTipoEvento());
        dto.setResumenEvento(model.getResumenEvento());
        dto.setArchivo(model.getArchivo());
        return dto;
    }


    private EventoCuerpoModel convertToModel(EventoCuerpoDTOCreate dto) {
        EventoCuerpoModel model = new EventoCuerpoModel();
        model.setFechaEvento(dto.getFechaEvento());
        model.setTipoEvento(dto.getTipoEvento());
        model.setResumenEvento(dto.getResumenEvento());
        // No asignamos archivo aquí, se asigna en createEvento después de subir a Cloudinary
        return model;
    }
}