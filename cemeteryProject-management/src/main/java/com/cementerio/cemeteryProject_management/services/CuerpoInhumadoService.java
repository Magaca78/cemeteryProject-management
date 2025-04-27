package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;
import com.cementerio.cemeteryProject_management.repositories.ICuerpoInhumadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CuerpoInhumadoService {

    @Autowired
    private ICuerpoInhumadoRepository cuerpoInhumadoRepository;

    public List<CuerpoInhumadoDTO> getAllCuerposInhumados() {
        return cuerpoInhumadoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CuerpoInhumadoDTO> getCuerpoInhumadoById(String idCadaver) {
        return cuerpoInhumadoRepository.findById(idCadaver)
                .map(this::convertToDTO);
    }

    public CuerpoInhumadoDTO createCuerpoInhumado(CuerpoInhumadoDTO dto) {
        CuerpoInhumadoModel model = convertToModel(dto);
        model.setIdCadaver(null); // Asegura que no se pase un idCadaver manualmente
        return convertToDTO(cuerpoInhumadoRepository.save(model));
    }

    public Optional<CuerpoInhumadoDTO> updateCuerpoInhumado(String idCadaver, CuerpoInhumadoDTO dto) {
        return cuerpoInhumadoRepository.findById(idCadaver).map(existing -> {
            existing.setNombre(dto.getNombre());
            existing.setApellido(dto.getApellido());
            existing.setDocumentoIdentidad(dto.getDocumentoIdentidad());
            existing.setNumeroProtocoloNecropsia(dto.getNumeroProtocoloNecropsia());
            existing.setCausaMuerte(dto.getCausaMuerte());
            existing.setFechaNacimiento(dto.getFechaNacimiento());
            existing.setFechaDefuncion(dto.getFechaDefuncion());
            existing.setFechaIngreso(dto.getFechaIngreso());
            existing.setFechaInhumacion(dto.getFechaInhumacion());
            existing.setFechaExhumacion(dto.getFechaExhumacion());
            existing.setFuncionarioReceptor(dto.getFuncionarioReceptor());
            existing.setCargoFuncionario(dto.getCargoFuncionario());
            existing.setAutoridadRemitente(dto.getAutoridadRemitente());
            existing.setCargoAutoridadRemitente(dto.getCargoAutoridadRemitente());
            existing.setAutoridadExhumacion(dto.getAutoridadExhumacion());
            existing.setCargoAutoridadExhumacion(dto.getCargoAutoridadExhumacion());
            existing.setEstado(dto.getEstado());
            existing.setObservaciones(dto.getObservaciones());
            return convertToDTO(cuerpoInhumadoRepository.save(existing));
        });
    }

    public boolean deleteCuerpoInhumado(String idCadaver) {
        if (cuerpoInhumadoRepository.existsById(idCadaver)) {
            cuerpoInhumadoRepository.deleteById(idCadaver);
            return true;
        }
        return false;
    }

    // Métodos auxiliares para conversión
    private CuerpoInhumadoDTO convertToDTO(CuerpoInhumadoModel model) {
        CuerpoInhumadoDTO dto = new CuerpoInhumadoDTO();
        dto.setIdCadaver(model.getIdCadaver());
        dto.setNombre(model.getNombre());
        dto.setApellido(model.getApellido());
        dto.setDocumentoIdentidad(model.getDocumentoIdentidad());
        dto.setNumeroProtocoloNecropsia(model.getNumeroProtocoloNecropsia());
        dto.setCausaMuerte(model.getCausaMuerte());
        dto.setFechaNacimiento(model.getFechaNacimiento());
        dto.setFechaDefuncion(model.getFechaDefuncion());
        dto.setFechaIngreso(model.getFechaIngreso());
        dto.setFechaInhumacion(model.getFechaInhumacion());
        dto.setFechaExhumacion(model.getFechaExhumacion());
        dto.setFuncionarioReceptor(model.getFuncionarioReceptor());
        dto.setCargoFuncionario(model.getCargoFuncionario());
        dto.setAutoridadRemitente(model.getAutoridadRemitente());
        dto.setCargoAutoridadRemitente(model.getCargoAutoridadRemitente());
        dto.setAutoridadExhumacion(model.getAutoridadExhumacion());
        dto.setCargoAutoridadExhumacion(model.getCargoAutoridadExhumacion());
        dto.setEstado(model.getEstado());
        dto.setObservaciones(model.getObservaciones());
        return dto;
    }

    private CuerpoInhumadoModel convertToModel(CuerpoInhumadoDTO dto) {
        CuerpoInhumadoModel model = new CuerpoInhumadoModel();
        model.setNombre(dto.getNombre());
        model.setApellido(dto.getApellido());
        model.setDocumentoIdentidad(dto.getDocumentoIdentidad());
        model.setNumeroProtocoloNecropsia(dto.getNumeroProtocoloNecropsia());
        model.setCausaMuerte(dto.getCausaMuerte());
        model.setFechaNacimiento(dto.getFechaNacimiento());
        model.setFechaDefuncion(dto.getFechaDefuncion());
        model.setFechaIngreso(dto.getFechaIngreso());
        model.setFechaInhumacion(dto.getFechaInhumacion());
        model.setFechaExhumacion(dto.getFechaExhumacion());
        model.setFuncionarioReceptor(dto.getFuncionarioReceptor());
        model.setCargoFuncionario(dto.getCargoFuncionario());
        model.setAutoridadRemitente(dto.getAutoridadRemitente());
        model.setCargoAutoridadRemitente(dto.getCargoAutoridadRemitente());
        model.setAutoridadExhumacion(dto.getAutoridadExhumacion());
        model.setCargoAutoridadExhumacion(dto.getCargoAutoridadExhumacion());
        model.setEstado(dto.getEstado());
        model.setObservaciones(dto.getObservaciones());
        return model;
    }

    public List<CuerpoInhumadoDTO> getCuerposNoAsignados() {
        return cuerpoInhumadoRepository.findCuerposNoAsignados()
            .stream()
            .map(this::convertToDTO)
            .toList();
    }
}