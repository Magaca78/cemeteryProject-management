package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.NichoDTO;
import com.cementerio.cemeteryProject_management.models.NichoModel;
import com.cementerio.cemeteryProject_management.repositories.INichoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NichoService {

    @Autowired
    private INichoRepository nichoRepository;

    public List<NichoDTO> getAllNichos() {
        return nichoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NichoDTO> getNichoById(String codigo) {
        return nichoRepository.findById(codigo)
                .map(this::convertToDTO);
    }

    public NichoDTO createNicho(NichoDTO dto) {
        NichoModel model = convertToModel(dto);
        model.setCodigo(null); // Asegura que no se pase un código manualmente
    
        return convertToDTO(nichoRepository.save(model));
    }
    
    public Optional<NichoDTO> updateNicho(String codigo, NichoDTO dto) {
        return nichoRepository.findById(codigo).map(existing -> {
            existing.setUbicacion(dto.getUbicacion());
            existing.setEstado(dto.getEstado());
            return convertToDTO(nichoRepository.save(existing));
        });
    }

    public boolean deleteNicho(String codigo) {
        if (nichoRepository.existsById(codigo)) {
            nichoRepository.deleteById(codigo);
            return true;
        }
        return false;
    }

    // Métodos auxiliares para conversión
    private NichoDTO convertToDTO(NichoModel model) {
        NichoDTO dto = new NichoDTO();
        dto.setCodigo(model.getCodigo());
        dto.setUbicacion(model.getUbicacion());
        dto.setEstado(model.getEstado());
        return dto;
    }

    private NichoModel convertToModel(NichoDTO dto) {
        NichoModel model = new NichoModel();
        // No se establece el código en el modelo, ya que se genera automáticamente
        model.setUbicacion(dto.getUbicacion());
        model.setEstado(dto.getEstado());
        return model;
    }
}
