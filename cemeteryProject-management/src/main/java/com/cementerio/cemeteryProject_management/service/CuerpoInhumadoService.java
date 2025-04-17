package com.cementerio.cemeteryProject_management.service;

import com.cementerio.cemeteryProject_management.dto.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.model.CuerpoInhumado;
import com.cementerio.cemeteryProject_management.repository.CuerpoInhumadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuerpoInhumadoService {

    private final CuerpoInhumadoRepository repository;

    public CuerpoInhumadoService(CuerpoInhumadoRepository repository) {
        this.repository = repository;
    }

    // ✅ Obtener todos
    public List<CuerpoInhumadoDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Obtener por ID
    public CuerpoInhumadoDTO getById(String idCadaver) {
        return toDTO(repository.findById(idCadaver)
                .orElseThrow(() -> new RuntimeException("Cuerpo no encontrado con ID: " + idCadaver)));
    }

    // ✅ Guardar nuevo
    public CuerpoInhumadoDTO save(CuerpoInhumadoDTO dto) {
        CuerpoInhumado entidad = toEntity(dto);
        return toDTO(repository.save(entidad));
    }

    // ✅ Actualizar existente
    public CuerpoInhumadoDTO update(String id, CuerpoInhumadoDTO dto) {
        CuerpoInhumado existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuerpo no encontrado con ID: " + id));

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setDocumentoIdentidad(dto.getDocumentoIdentidad());
        existente.setNumeroProtocoloNecropsia(dto.getNumeroProtocoloNecropsia());
        existente.setCausaMuerte(dto.getCausaMuerte());
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setFechaDefuncion(dto.getFechaDefuncion());
        existente.setFechaIngreso(dto.getFechaIngreso());
        existente.setFechaInhumacion(dto.getFechaInhumacion());
        existente.setFechaExhumacion(dto.getFechaExhumacion());
        existente.setFuncionarioReceptor(dto.getFuncionarioReceptor());
        existente.setCargoFuncionario(dto.getCargoFuncionario());
        existente.setAutoridadRemitente(dto.getAutoridadRemitente());
        existente.setCargoAutoridadRemitente(dto.getCargoAutoridadRemitente());
        existente.setAutoridadExhumacion(dto.getAutoridadExhumacion());
        existente.setCargoAutoridadExhumacion(dto.getCargoAutoridadExhumacion());
        existente.setEstado(CuerpoInhumado.EstadoCuerpo.valueOf(dto.getEstado()));
        existente.setObservaciones(dto.getObservaciones());

        return toDTO(repository.save(existente));
    }

    // ✅ Eliminar por ID
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: cuerpo no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    // 🔄 Conversión Entidad → DTO
    private CuerpoInhumadoDTO toDTO(CuerpoInhumado entidad) {
        CuerpoInhumadoDTO dto = new CuerpoInhumadoDTO();
        dto.setIdCadaver(entidad.getIdCadaver());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setDocumentoIdentidad(entidad.getDocumentoIdentidad());
        dto.setNumeroProtocoloNecropsia(entidad.getNumeroProtocoloNecropsia());
        dto.setCausaMuerte(entidad.getCausaMuerte());
        dto.setFechaNacimiento(entidad.getFechaNacimiento());
        dto.setFechaDefuncion(entidad.getFechaDefuncion());
        dto.setFechaIngreso(entidad.getFechaIngreso());
        dto.setFechaInhumacion(entidad.getFechaInhumacion());
        dto.setFechaExhumacion(entidad.getFechaExhumacion());
        dto.setFuncionarioReceptor(entidad.getFuncionarioReceptor());
        dto.setCargoFuncionario(entidad.getCargoFuncionario());
        dto.setAutoridadRemitente(entidad.getAutoridadRemitente());
        dto.setCargoAutoridadRemitente(entidad.getCargoAutoridadRemitente());
        dto.setAutoridadExhumacion(entidad.getAutoridadExhumacion());
        dto.setCargoAutoridadExhumacion(entidad.getCargoAutoridadExhumacion());
        dto.setEstado(entidad.getEstado().name());
        dto.setObservaciones(entidad.getObservaciones());
        return dto;
    }

    // 🔄 Conversión DTO → Entidad
    private CuerpoInhumado toEntity(CuerpoInhumadoDTO dto) {
        CuerpoInhumado entidad = new CuerpoInhumado();
        entidad.setIdCadaver(dto.getIdCadaver());
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setDocumentoIdentidad(dto.getDocumentoIdentidad());
        entidad.setNumeroProtocoloNecropsia(dto.getNumeroProtocoloNecropsia());
        entidad.setCausaMuerte(dto.getCausaMuerte());
        entidad.setFechaNacimiento(dto.getFechaNacimiento());
        entidad.setFechaDefuncion(dto.getFechaDefuncion());
        entidad.setFechaIngreso(dto.getFechaIngreso());
        entidad.setFechaInhumacion(dto.getFechaInhumacion());
        entidad.setFechaExhumacion(dto.getFechaExhumacion());
        entidad.setFuncionarioReceptor(dto.getFuncionarioReceptor());
        entidad.setCargoFuncionario(dto.getCargoFuncionario());
        entidad.setAutoridadRemitente(dto.getAutoridadRemitente());
        entidad.setCargoAutoridadRemitente(dto.getCargoAutoridadRemitente());
        entidad.setAutoridadExhumacion(dto.getAutoridadExhumacion());
        entidad.setCargoAutoridadExhumacion(dto.getCargoAutoridadExhumacion());
        entidad.setEstado(CuerpoInhumado.EstadoCuerpo.valueOf(dto.getEstado()));
        entidad.setObservaciones(dto.getObservaciones());
        return entidad;
    }
}
