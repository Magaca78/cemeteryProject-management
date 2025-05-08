package com.cementerio.cemeteryProject_management.services;

import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.dtos.NichoCuerpoDTO;
import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;
import com.cementerio.cemeteryProject_management.models.NichoCuerpoModel;
import com.cementerio.cemeteryProject_management.models.NichoModel;
import com.cementerio.cemeteryProject_management.repositories.ICuerpoInhumadoRepository;
import com.cementerio.cemeteryProject_management.repositories.INichoCuerpoRepository;
import com.cementerio.cemeteryProject_management.repositories.INichoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NichoCuerpoServiceTest {

    @InjectMocks
    private NichoCuerpoService nichoCuerpoService;

    @Mock
    private INichoCuerpoRepository nichoCuerpoRepository;

    @Mock
    private INichoRepository nichoRepository;

    @Mock
    private ICuerpoInhumadoRepository cuerpoInhumadoRepository;

    @Mock
    private CuerpoInhumadoService cuerpoInhumadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        NichoCuerpoModel model = new NichoCuerpoModel("1", new CuerpoInhumadoModel(), new NichoModel());
        when(nichoCuerpoRepository.findAll()).thenReturn(List.of(model));

        List<NichoCuerpoDTO> result = nichoCuerpoService.getAll();

        assertEquals(1, result.size());
        verify(nichoCuerpoRepository).findAll();
    }

    @Test
    void testGetByIdExists() {
        NichoCuerpoModel model = new NichoCuerpoModel("1", new CuerpoInhumadoModel(), new NichoModel());
        when(nichoCuerpoRepository.findById("1")).thenReturn(Optional.of(model));

        Optional<NichoCuerpoDTO> result = nichoCuerpoService.getById("1");

        assertTrue(result.isPresent());
    }

    @Test
    void testGetByIdNotExists() {
        when(nichoCuerpoRepository.findById("1")).thenReturn(Optional.empty());

        Optional<NichoCuerpoDTO> result = nichoCuerpoService.getById("1");

        assertFalse(result.isPresent());
    }

    @Test
    void testGetCuerpoByCodigoNichoExists() {
        NichoCuerpoModel model = new NichoCuerpoModel("1", new CuerpoInhumadoModel(), new NichoModel());
        CuerpoInhumadoDTO dto = new CuerpoInhumadoDTO();
        when(nichoCuerpoRepository.findByNicho_Codigo("N001")).thenReturn(Optional.of(model));
        when(cuerpoInhumadoService.getCuerpoInhumadoById(null)).thenReturn(Optional.of(dto));

        Optional<CuerpoInhumadoDTO> result = nichoCuerpoService.getCuerpoByCodigoNicho("N001");

        assertTrue(result.isPresent());
    }

    @Test
    void testGetCuerpoByCodigoNichoNotExists() {
        when(nichoCuerpoRepository.findByNicho_Codigo("N001")).thenReturn(Optional.empty());

        Optional<CuerpoInhumadoDTO> result = nichoCuerpoService.getCuerpoByCodigoNicho("N001");

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateRelacionCorrecta() {
        CuerpoInhumadoModel cuerpo = new CuerpoInhumadoModel();
        cuerpo.setIdCadaver("C001");

        NichoModel nicho = new NichoModel();
        nicho.setCodigo("N001");
        nicho.setEstado(NichoModel.EstadoNicho.DISPONIBLE);

        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        dto.setIdCadaver("C001");
        dto.setCodigoNicho("N001");

        NichoCuerpoModel model = new NichoCuerpoModel("1", cuerpo, nicho);

        when(cuerpoInhumadoRepository.findById("C001")).thenReturn(Optional.of(cuerpo));
        when(nichoRepository.findById("N001")).thenReturn(Optional.of(nicho));
        when(nichoCuerpoRepository.save(any())).thenReturn(model);

        NichoCuerpoDTO result = nichoCuerpoService.create(dto);

        assertEquals("C001", result.getIdCadaver());
        assertEquals("N001", result.getCodigoNicho());
        verify(nichoRepository).save(nicho);
    }

    @Test
    void testCreateRelacionCuerpoInexistente() {
        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        dto.setIdCadaver("C001");
        dto.setCodigoNicho("N001");

        when(cuerpoInhumadoRepository.findById("C001")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> nichoCuerpoService.create(dto));
    }

    @Test
    void testCreateRelacionNichoInexistente() {
        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        dto.setIdCadaver("C001");
        dto.setCodigoNicho("N001");

        CuerpoInhumadoModel cuerpo = new CuerpoInhumadoModel();
        cuerpo.setIdCadaver("C001");

        when(cuerpoInhumadoRepository.findById("C001")).thenReturn(Optional.of(cuerpo));
        when(nichoRepository.findById("N001")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> nichoCuerpoService.create(dto));
    }

    @Test
    void testUpdateRelacionExiste() {
        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        dto.setIdCadaver("C001");
        dto.setCodigoNicho("N001");

        CuerpoInhumadoModel cuerpo = new CuerpoInhumadoModel();
        cuerpo.setIdCadaver("C001");

        NichoModel nicho = new NichoModel();
        nicho.setCodigo("N001");

        NichoCuerpoModel model = new NichoCuerpoModel("1", cuerpo, nicho);

        when(nichoCuerpoRepository.findById("1")).thenReturn(Optional.of(model));
        when(cuerpoInhumadoRepository.findById("C001")).thenReturn(Optional.of(cuerpo));
        when(nichoRepository.findById("N001")).thenReturn(Optional.of(nicho));
        when(nichoCuerpoRepository.save(any())).thenReturn(model);

        Optional<NichoCuerpoDTO> result = nichoCuerpoService.update("1", dto);

        assertTrue(result.isPresent());
        assertEquals("C001", result.get().getIdCadaver());
    }

    @Test
    void testUpdateRelacionNoExiste() {
        NichoCuerpoDTO dto = new NichoCuerpoDTO();
        when(nichoCuerpoRepository.findById("1")).thenReturn(Optional.empty());

        Optional<NichoCuerpoDTO> result = nichoCuerpoService.update("1", dto);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteRelacionExiste() {
        NichoModel nicho = new NichoModel();
        nicho.setEstado(NichoModel.EstadoNicho.OCUPADO);
        NichoCuerpoModel model = new NichoCuerpoModel("1", new CuerpoInhumadoModel(), nicho);

        when(nichoCuerpoRepository.existsById("1")).thenReturn(true);
        when(nichoCuerpoRepository.findById("1")).thenReturn(Optional.of(model));

        boolean result = nichoCuerpoService.delete("1");

        assertTrue(result);
        verify(nichoCuerpoRepository).deleteById("1");
        verify(nichoRepository).save(nicho);
    }

    @Test
    void testDeleteRelacionNoExiste() {
        when(nichoCuerpoRepository.existsById("1")).thenReturn(false);

        boolean result = nichoCuerpoService.delete("1");

        assertFalse(result);
    }
}
