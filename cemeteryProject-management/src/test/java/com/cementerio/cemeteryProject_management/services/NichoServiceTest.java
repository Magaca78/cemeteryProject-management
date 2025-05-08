package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.NichoDTO;
import com.cementerio.cemeteryProject_management.models.NichoModel;
import com.cementerio.cemeteryProject_management.models.NichoModel.EstadoNicho;
import com.cementerio.cemeteryProject_management.repositories.INichoCuerpoRepository;
import com.cementerio.cemeteryProject_management.repositories.INichoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NichoServiceTest {

    @Mock
    private INichoRepository nichoRepository;

    @Mock
    private INichoCuerpoRepository nichoCuerpoRepository;

    @InjectMocks
    private NichoService nichoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNicho_deberiaGuardarYRetornarDTO() {
        NichoDTO dto = new NichoDTO();
        dto.setUbicacion("Sector A");
        dto.setEstado(EstadoNicho.DISPONIBLE);

        when(nichoRepository.save(any(NichoModel.class))).thenAnswer(i -> {
            NichoModel model = i.getArgument(0);
            model.setCodigo(UUID.randomUUID().toString());
            return model;
        });

        NichoDTO result = nichoService.createNicho(dto);

        assertNotNull(result.getCodigo());
        assertEquals("Sector A", result.getUbicacion());
        assertEquals(EstadoNicho.DISPONIBLE, result.getEstado());
        verify(nichoRepository, times(1)).save(any());
    }

    @Test
    void getAllNichos_deberiaRetornarLista() {
        List<NichoModel> modelos = List.of(
                new NichoModel("1", "Ub1", EstadoNicho.DISPONIBLE),
                new NichoModel("2", "Ub2", EstadoNicho.OCUPADO)
        );
        when(nichoRepository.findAll()).thenReturn(modelos);

        List<NichoDTO> result = nichoService.getAllNichos();

        assertEquals(2, result.size());
        verify(nichoRepository).findAll();
    }

    @Test
    void getNichoById_existente_deberiaRetornarDTO() {
        String codigo = "ABC123";
        NichoModel model = new NichoModel(codigo, "Ub1", EstadoNicho.DISPONIBLE);
        when(nichoRepository.findById(codigo)).thenReturn(Optional.of(model));
        when(nichoCuerpoRepository.existsByNichoCodigo(codigo)).thenReturn(false);

        Optional<NichoDTO> result = nichoService.getNichoById(codigo);

        assertTrue(result.isPresent());
        assertEquals(codigo, result.get().getCodigo());
    }

    @Test
    void getNichoById_inexistente_deberiaRetornarVacio() {
        when(nichoRepository.findById("X")).thenReturn(Optional.empty());

        Optional<NichoDTO> result = nichoService.getNichoById("X");

        assertTrue(result.isEmpty());
    }

    @Test
    void updateNicho_existente_deberiaActualizarYRetornarDTO() {
        String codigo = "ABC123";
        NichoDTO dto = new NichoDTO();
        dto.setUbicacion("NuevaUbicacion");
        dto.setEstado(EstadoNicho.OCUPADO);

        NichoModel existente = new NichoModel(codigo, "AntiguaUbicacion", EstadoNicho.DISPONIBLE);
        when(nichoRepository.findById(codigo)).thenReturn(Optional.of(existente));
        when(nichoRepository.save(any())).thenReturn(existente);

        Optional<NichoDTO> result = nichoService.updateNicho(codigo, dto);

        assertTrue(result.isPresent());
        assertEquals("NuevaUbicacion", result.get().getUbicacion());
        assertEquals(EstadoNicho.OCUPADO, result.get().getEstado());
    }

    @Test
    void updateNicho_inexistente_deberiaRetornarVacio() {
        when(nichoRepository.findById("nope")).thenReturn(Optional.empty());

        Optional<NichoDTO> result = nichoService.updateNicho("nope", new NichoDTO());

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteNicho_existente_deberiaEliminarYRetornarTrue() {
        when(nichoRepository.existsById("1")).thenReturn(true);

        boolean result = nichoService.deleteNicho("1");

        assertTrue(result);
        verify(nichoRepository).deleteById("1");
    }

    @Test
    void deleteNicho_inexistente_deberiaRetornarFalse() {
        when(nichoRepository.existsById("X")).thenReturn(false);

        boolean result = nichoService.deleteNicho("X");

        assertFalse(result);
    }
}
    