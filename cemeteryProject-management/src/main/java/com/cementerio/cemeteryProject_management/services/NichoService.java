package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.NichoDTO;
import com.cementerio.cemeteryProject_management.models.NichoModel;
import com.cementerio.cemeteryProject_management.models.NichoModel.EstadoNicho;
import com.cementerio.cemeteryProject_management.repositories.INichoCuerpoRepository;
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

    @Autowired
    private INichoCuerpoRepository nichoCuerpoRepository;

    public void sincronizarEstadosDeNichos() {
        List<NichoModel> todos = nichoRepository.findAll();
        for (NichoModel nicho : todos) {
            // 1) Si está en mantenimiento, lo respetamos
            if (nicho.getEstado() == NichoModel.EstadoNicho.MANTENIMIENTO) {
                continue;
            }

            // 2) Sino, comprobamos la intermedia
            boolean tieneCuerpo = nichoCuerpoRepository.existsByNichoCodigo(nicho.getCodigo());
            NichoModel.EstadoNicho nuevoEstado = tieneCuerpo
                ? NichoModel.EstadoNicho.OCUPADO
                : NichoModel.EstadoNicho.DISPONIBLE;

            // 3) Sólo guardamos si cambió
            if (nicho.getEstado() != nuevoEstado) {
                nicho.setEstado(nuevoEstado);
                nichoRepository.save(nicho);
            }
        }
    }

    public List<NichoDTO> getAllNichos() {
        return nichoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NichoDTO> getNichoById(String codigo) {
        return nichoRepository.findById(codigo)
            .map(nichoModel -> {
                NichoDTO dto = convertToDTO(nichoModel);
                // Validación extra en la tabla intermedia:
                boolean ocupadoEnIntermedia = nichoCuerpoRepository.existsByNichoCodigo(codigo);
                if (ocupadoEnIntermedia) {
                    dto.setEstado(EstadoNicho.OCUPADO);
                }
                return dto;
            });
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

    public List<NichoDTO> getNichosDisponibles() {
        return nichoRepository.findByEstado(NichoModel.EstadoNicho.DISPONIBLE)
            .stream()
            .map(this::convertToDTO)
            .toList();
    }
    
}
