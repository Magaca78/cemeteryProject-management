package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.dtos.NichoCuerpoDTO;
import com.cementerio.cemeteryProject_management.dtos.NichoDTO;
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
    
    @Autowired
    private CuerpoInhumadoService cuerpoInhumadoService;


    public List<NichoCuerpoDTO> getAll() {
        return nichoCuerpoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NichoCuerpoDTO> getById(String id) {
        return nichoCuerpoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<CuerpoInhumadoDTO> getCuerpoByCodigoNicho(String codigoNicho) {
        return nichoCuerpoRepository.findByNicho_Codigo(codigoNicho)
                .map(nichoCuerpo -> cuerpoInhumadoService.getCuerpoInhumadoById(nichoCuerpo.getCuerpoInhumado().getIdCadaver()))
                .orElse(Optional.empty());
    }

    public NichoCuerpoDTO create(NichoCuerpoDTO dto) {
        // 1) traer entidades padre
        CuerpoInhumadoModel cuerpo = cuerpoInhumadoRepository
            .findById(dto.getIdCadaver())
            .orElseThrow(() -> new IllegalArgumentException("Cuerpo no existe"));
        NichoModel nicho = nichoRepository
            .findById(dto.getCodigoNicho())
            .orElseThrow(() -> new IllegalArgumentException("Nicho no existe"));

        // 2) crear y guardar la relación
        NichoCuerpoModel rel = new NichoCuerpoModel();
        rel.setCuerpoInhumado(cuerpo);
        rel.setNicho(nicho);
        rel = nichoCuerpoRepository.save(rel);

        // 3) actualizar estado del nicho si no está en mantenimiento
        if (nicho.getEstado() != NichoModel.EstadoNicho.MANTENIMIENTO) {
            nicho.setEstado(NichoModel.EstadoNicho.OCUPADO);
            nichoRepository.save(nicho);
        }

        // 4) mapear a DTO y devolver
        return convertToDTO(rel);
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

            nichoCuerpoRepository.findById(id).ifPresent(relacion -> {
                NichoModel nicho = relacion.getNicho();
                
                // Actualizar el estado del nicho a DISPONIBLE si no está en mantenimiento
                if (nicho.getEstado() != NichoModel.EstadoNicho.MANTENIMIENTO) {
                    nicho.setEstado(NichoModel.EstadoNicho.DISPONIBLE);
                    nichoRepository.save(nicho);
                }
            });

            nichoCuerpoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<NichoCuerpoDTO> getByCodigoNicho(String codigoNicho) {
        return nichoCuerpoRepository.findByNicho_Codigo(codigoNicho)
                .map(this::convertToDTO);
    }

    public Optional<NichoDTO> getNichoAsignadoPorCuerpo(String idCadaver) {
    return nichoCuerpoRepository.findByCuerpoInhumado_IdCadaver(idCadaver)
        .map(NichoCuerpoModel::getNicho)
        .map(this::convertToNichoDTO); 
    }

    // Conversiones

    private NichoCuerpoDTO convertToDTO(NichoCuerpoModel model) {
        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        dto.setId(model.getId());
        dto.setIdCadaver(model.getCuerpoInhumado().getIdCadaver());
        dto.setCodigoNicho(model.getNicho().getCodigo());
        return dto;
    }

    private NichoDTO convertToNichoDTO(NichoModel model) {
        NichoDTO dto = new NichoDTO();
        dto.setCodigo(model.getCodigo());
        dto.setUbicacion(model.getUbicacion());
        dto.setEstado(model.getEstado());
        return dto;
    }
}
