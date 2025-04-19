package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.NichoCuerpoDTO;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;
import com.cementerio.cemeteryProject_management.models.NichoCuerpoModel;
import com.cementerio.cemeteryProject_management.models.NichoModel;
import com.cementerio.cemeteryProject_management.repositories.ICuerpoInhumadoRepository;
import com.cementerio.cemeteryProject_management.repositories.INichoCuerpoRepository;
import com.cementerio.cemeteryProject_management.repositories.INichoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NichoCuerpoService {

    @Autowired
    private INichoCuerpoRepository nichoCuerpoRepository;

    @Autowired
    private INichoRepository nichoRepository;

    @Autowired
    private ICuerpoInhumadoRepository cuerpoInhumadoRepository;

    public List<NichoCuerpoDTO> getAll() {
        return nichoCuerpoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NichoCuerpoDTO> getById(String id) {
        return nichoCuerpoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public NichoCuerpoDTO create(NichoCuerpoDTO dto) {
        NichoCuerpoModel model = convertToModel(dto);
        model.setId(null); // Para que se genere automáticamente
        return convertToDTO(nichoCuerpoRepository.save(model));
    }

    public Optional<NichoCuerpoDTO> update(String id, NichoCuerpoDTO dto) {
        return nichoCuerpoRepository.findById(id).map(existing -> {
            existing.setCuerpoInhumado(cuerpoInhumadoRepository.findById(dto.getIdCadaver()).orElseThrow());
            existing.setNicho(nichoRepository.findById(dto.getCodigoNicho()).orElseThrow());
            return convertToDTO(nichoCuerpoRepository.save(existing));
        });
    }

    public boolean delete(String id) {
        if (nichoCuerpoRepository.existsById(id)) {
            nichoCuerpoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Conversiones

    private NichoCuerpoDTO convertToDTO(NichoCuerpoModel model) {
        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        dto.setId(model.getId());
        dto.setIdCadaver(model.getCuerpoInhumado().getIdCadaver());
        dto.setCodigoNicho(model.getNicho().getCodigo());
        return dto;
    }

    private NichoCuerpoModel convertToModel(NichoCuerpoDTO dto) {
        NichoCuerpoModel model = new NichoCuerpoModel();
        model.setCuerpoInhumado(cuerpoInhumadoRepository.findById(dto.getIdCadaver()).orElseThrow());
        model.setNicho(nichoRepository.findById(dto.getCodigoNicho()).orElseThrow());
        return model;
    }
}
