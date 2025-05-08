package com.cementerio.cemeteryProject_management.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;
import com.cementerio.cemeteryProject_management.repositories.ICuerpoInhumadoRepository;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel.EstadoCuerpo;

class CuerpoInhumadoServiceTest {

    @Mock
    private ICuerpoInhumadoRepository cuerpoInhumadoRepository;

    @InjectMocks
    private CuerpoInhumadoService cuerpoInhumadoService;

    private CuerpoInhumadoModel cuerpo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cuerpo = new CuerpoInhumadoModel();
        cuerpo.setIdCadaver("123");
        cuerpo.setNombre("Juan");
        cuerpo.setApellido("Perez");
        cuerpo.setDocumentoIdentidad("12345678");
        cuerpo.setNumeroProtocoloNecropsia("NP12345");
        cuerpo.setCausaMuerte("Accidente");
        cuerpo.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        cuerpo.setFechaDefuncion(LocalDate.of(2020, 5, 15));
        cuerpo.setFechaIngreso(LocalDateTime.now());
        cuerpo.setFechaInhumacion(LocalDate.of(2020, 5, 16));
        cuerpo.setFechaExhumacion(LocalDate.of(2025, 5, 15));
        cuerpo.setFuncionarioReceptor("Funcionario 1");
        cuerpo.setCargoFuncionario("Cargo 1");
        cuerpo.setAutoridadRemitente("Autoridad 1");
        cuerpo.setCargoAutoridadRemitente("Cargo Autoridad 1");
        cuerpo.setAutoridadExhumacion("Autoridad Exhumacion");
        cuerpo.setCargoAutoridadExhumacion("Cargo Exhumacion");
        cuerpo.setEstado(EstadoCuerpo.INHUMADO);
        cuerpo.setObservaciones("Observaciones");
    }

    @Test
    void createCuerpoInhumado_validData_createsCuerpoInhumado() {
        // Arrange
        CuerpoInhumadoDTO dto = new CuerpoInhumadoDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setDocumentoIdentidad("12345678");
        dto.setNumeroProtocoloNecropsia("NP12345");
        dto.setCausaMuerte("Accidente");
        dto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        dto.setFechaDefuncion(LocalDate.of(2020, 5, 15));
        dto.setFechaIngreso(LocalDateTime.now());
        dto.setFechaInhumacion(LocalDate.of(2020, 5, 16));
        dto.setFechaExhumacion(LocalDate.of(2025, 5, 15));
        dto.setFuncionarioReceptor("Funcionario 1");
        dto.setCargoFuncionario("Cargo 1");
        dto.setAutoridadRemitente("Autoridad 1");
        dto.setCargoAutoridadRemitente("Cargo Autoridad 1");
        dto.setAutoridadExhumacion("Autoridad Exhumacion");
        dto.setCargoAutoridadExhumacion("Cargo Exhumacion");
        dto.setEstado(EstadoCuerpo.INHUMADO);
        dto.setObservaciones("Observaciones");

        // Act
        when(cuerpoInhumadoRepository.save(any(CuerpoInhumadoModel.class))).thenReturn(cuerpo);
        CuerpoInhumadoDTO result = cuerpoInhumadoService.createCuerpoInhumado(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("Perez", result.getApellido());
        assertEquals("12345678", result.getDocumentoIdentidad());
    }

    @Test
    void getAllCuerposInhumados_returnsListOfCuerpos() {
        // Arrange
        CuerpoInhumadoModel cuerpo1 = new CuerpoInhumadoModel();
        CuerpoInhumadoModel cuerpo2 = new CuerpoInhumadoModel();
        List<CuerpoInhumadoModel> cuerposList = Arrays.asList(cuerpo1, cuerpo2);
        when(cuerpoInhumadoRepository.findAll()).thenReturn(cuerposList);

        // Act
        List<CuerpoInhumadoDTO> result = cuerpoInhumadoService.getAllCuerposInhumados();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getCuerpoInhumadoById_existingId_returnsCuerpoInhumadoDTO() {
        // Arrange
        when(cuerpoInhumadoRepository.findById("123")).thenReturn(Optional.of(cuerpo));

        // Act
        Optional<CuerpoInhumadoDTO> result = cuerpoInhumadoService.getCuerpoInhumadoById("123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("123", result.get().getIdCadaver());
    }

    @Test
    void getCuerpoInhumadoById_nonExistingId_returnsEmpty() {
        // Arrange
        when(cuerpoInhumadoRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<CuerpoInhumadoDTO> result = cuerpoInhumadoService.getCuerpoInhumadoById("999");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateCuerpoInhumado_validData_updatesCuerpoInhumado() {
        // Arrange
        CuerpoInhumadoDTO dto = new CuerpoInhumadoDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setDocumentoIdentidad("12345678");
        dto.setNumeroProtocoloNecropsia("NP12345");
        dto.setCausaMuerte("Accidente");
        dto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        dto.setFechaDefuncion(LocalDate.of(2020, 5, 15));
        dto.setFechaIngreso(LocalDateTime.now());
        dto.setFechaInhumacion(LocalDate.of(2020, 5, 16));
        dto.setFechaExhumacion(LocalDate.of(2025, 5, 15));
        dto.setFuncionarioReceptor("Funcionario 1");
        dto.setCargoFuncionario("Cargo 1");
        dto.setAutoridadRemitente("Autoridad 1");
        dto.setCargoAutoridadRemitente("Cargo Autoridad 1");
        dto.setAutoridadExhumacion("Autoridad Exhumacion");
        dto.setCargoAutoridadExhumacion("Cargo Exhumacion");
        dto.setEstado(EstadoCuerpo.EXHUMADO);
        dto.setObservaciones("Observaciones");

        // Act
        when(cuerpoInhumadoRepository.findById("123")).thenReturn(Optional.of(cuerpo));
        when(cuerpoInhumadoRepository.save(any(CuerpoInhumadoModel.class))).thenReturn(cuerpo);
        Optional<CuerpoInhumadoDTO> result = cuerpoInhumadoService.updateCuerpoInhumado("123", dto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getNombre());
        assertEquals("Perez", result.get().getApellido());
        assertEquals(EstadoCuerpo.EXHUMADO, result.get().getEstado());
    }

    @Test
    void updateCuerpoInhumado_nonExistingId_returnsEmpty() {
        // Arrange
        CuerpoInhumadoDTO dto = new CuerpoInhumadoDTO();
        dto.setNombre("Juan");

        when(cuerpoInhumadoRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<CuerpoInhumadoDTO> result = cuerpoInhumadoService.updateCuerpoInhumado("999", dto);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteCuerpoInhumado_existingId_deletesCuerpoInhumado() {
        // Arrange
        when(cuerpoInhumadoRepository.existsById("123")).thenReturn(true);

        // Act
        boolean result = cuerpoInhumadoService.deleteCuerpoInhumado("123");

        // Assert
        assertTrue(result);
        verify(cuerpoInhumadoRepository, times(1)).deleteById("123");
    }

    @Test
    void deleteCuerpoInhumado_nonExistingId_returnsFalse() {
        // Arrange
        when(cuerpoInhumadoRepository.existsById("999")).thenReturn(false);

        // Act
        boolean result = cuerpoInhumadoService.deleteCuerpoInhumado("999");

        // Assert
        assertFalse(result);
        verify(cuerpoInhumadoRepository, times(0)).deleteById("999");
    }
}
