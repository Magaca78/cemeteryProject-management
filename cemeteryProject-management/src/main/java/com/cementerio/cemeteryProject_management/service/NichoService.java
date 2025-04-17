package com.cementerio.cemeteryProject_management.service;

import com.cementerio.cemeteryProject_management.dto.NichoDTO;
import com.cementerio.cemeteryProject_management.model.Nicho;
import com.cementerio.cemeteryProject_management.repository.NichoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NichoService {

    private final NichoRepository repository;

    public NichoService(NichoRepository repository) {
        this.repository = repository;
    }

    public List<NichoDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public NichoDTO toDTO(Nicho entidad) {
        NichoDTO dto = new NichoDTO();
        dto.setCodigo(entidad.getCodigo());
        dto.setUbicacion(entidad.getUbicacion());
        dto.setEstado(entidad.getEstado().name());
        return dto;
    }

    public Nicho toEntity(NichoDTO dto) {
        Nicho entidad = new Nicho();
        entidad.setCodigo(dto.getCodigo());
        entidad.setUbicacion(dto.getUbicacion());
        entidad.setEstado(Nicho.EstadoNicho.valueOf(dto.getEstado()));
        return entidad;
    }

    public NichoDTO save(NichoDTO dto) {
        Nicho entidad = toEntity(dto);
        return toDTO(repository.save(entidad));
    }

    public NichoDTO getById(String codigo) {
        Nicho entidad = repository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Nicho no encontrado con código: " + codigo));
        return toDTO(entidad);
    }
    
    public NichoDTO update(String codigo, NichoDTO dto) {
        Nicho existente = repository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Nicho no encontrado con código: " + codigo));
        existente.setUbicacion(dto.getUbicacion());
        existente.setEstado(Nicho.EstadoNicho.valueOf(dto.getEstado()));
        return toDTO(repository.save(existente));
    }
    
    public void delete(String codigo) {
        repository.deleteById(codigo);
    }
    
}
