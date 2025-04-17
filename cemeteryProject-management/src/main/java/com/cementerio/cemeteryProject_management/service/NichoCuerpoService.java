package com.cementerio.cemeteryProject_management.service;

import com.cementerio.cemeteryProject_management.dto.NichoCuerpoDTO;
import com.cementerio.cemeteryProject_management.model.CuerpoInhumado;
import com.cementerio.cemeteryProject_management.model.Nicho;
import com.cementerio.cemeteryProject_management.model.NichoCuerpo;
import com.cementerio.cemeteryProject_management.repository.CuerpoInhumadoRepository;
import com.cementerio.cemeteryProject_management.repository.NichoRepository;
import com.cementerio.cemeteryProject_management.repository.NichoCuerpoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NichoCuerpoService {

    private final NichoCuerpoRepository repository;
    private final CuerpoInhumadoRepository cuerpoRepository;
    private final NichoRepository nichoRepository;

    public NichoCuerpoService(NichoCuerpoRepository repository,
                              CuerpoInhumadoRepository cuerpoRepository,
                              NichoRepository nichoRepository) {
        this.repository = repository;
        this.cuerpoRepository = cuerpoRepository;
        this.nichoRepository = nichoRepository;
    }

    public List<NichoCuerpoDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public NichoCuerpoDTO toDTO(NichoCuerpo entidad) {
        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        dto.setId(entidad.getId().toString());
        dto.setIdCadaver(entidad.getCuerpoInhumado().getIdCadaver());
        dto.setCodigoNicho(entidad.getNicho().getCodigo());
        return dto;
    }

    public NichoCuerpo toEntity(NichoCuerpoDTO dto) {
        NichoCuerpo entidad = new NichoCuerpo();
        CuerpoInhumado cuerpo = cuerpoRepository.findById(dto.getIdCadaver())
                .orElseThrow(() -> new RuntimeException("Cuerpo no encontrado: " + dto.getIdCadaver()));
                Nicho nicho = nichoRepository.findById(dto.getCodigoNicho())
                .orElseThrow(() -> new RuntimeException("Nicho no encontrado con código: " + dto.getCodigoNicho()));
        entidad.setCuerpoInhumado(cuerpo);
        entidad.setNicho(nicho);
        return entidad;
    }

    public NichoCuerpoDTO save(NichoCuerpoDTO dto) {
        NichoCuerpo entidad = toEntity(dto);
        return toDTO(repository.save(entidad));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
    
    public NichoCuerpoDTO getById(String id) {
        return toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NichoCuerpo no encontrado con id: " + id)));
    }
    
}
