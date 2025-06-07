package com.cementerio.cemeteryProject_management.controllers;

import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoDTO;
import com.cementerio.cemeteryProject_management.dtos.CuerpoInhumadoSTRDTO;
import com.cementerio.cemeteryProject_management.services.CuerpoInhumadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cuerposinhumados")
public class CuerpoInhumadoController {

    @Autowired
    private CuerpoInhumadoService cuerpoInhumadoService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public List<CuerpoInhumadoDTO> getAll() {
        return cuerpoInhumadoService.getAllCuerposInhumados();
    }

    @GetMapping("/{idCadaver}")
    public ResponseEntity<CuerpoInhumadoDTO> getById(@PathVariable String idCadaver) {
        return cuerpoInhumadoService.getCuerpoInhumadoById(idCadaver)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CuerpoInhumadoDTO create(@RequestBody CuerpoInhumadoDTO dto) {
        return cuerpoInhumadoService.createCuerpoInhumado(dto);
    }

    @PutMapping("/{idCadaver}")
    public ResponseEntity<CuerpoInhumadoDTO> update(@PathVariable String idCadaver, @RequestBody CuerpoInhumadoDTO dto) {
        return cuerpoInhumadoService.updateCuerpoInhumado(idCadaver, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idCadaver}")
    public ResponseEntity<Void> delete(@PathVariable String idCadaver) {
        if (cuerpoInhumadoService.deleteCuerpoInhumado(idCadaver)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/no-asignados")
    public List<CuerpoInhumadoDTO> getCuerposNoAsignados() {
        return cuerpoInhumadoService.getCuerposNoAsignados();
    }

    @GetMapping("/ultimos")
    public List<CuerpoInhumadoDTO> getUltimosCuerpos(@RequestParam(defaultValue = "8") int cantidad) {
        return cuerpoInhumadoService.getLatestCuerposInhumados(cantidad);
    }

    /**
     * Busca cuerpos por nombre, apellido, documento de identidad o id
     * @param query Término de búsqueda
     * @return Lista de cuerpos que coinciden con la búsqueda
     */
    @GetMapping("/search")
    public List<CuerpoInhumadoDTO> searchCuerpos(@RequestParam String query) {
        return cuerpoInhumadoService.searchCuerpos(query);
    }

    @PostMapping("/from-form")
    public ResponseEntity<CuerpoInhumadoSTRDTO> createFromForm(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Prepare the multipart request for FastAPI
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send request to FastAPI
            String fastApiUrl = "http://localhost:8089/api/v1/process-form";
            ResponseEntity<CuerpoInhumadoSTRDTO> response = restTemplate.postForEntity(
                fastApiUrl, requestEntity, CuerpoInhumadoSTRDTO.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.status(500).build();
            }

            // Map FastApiCuerpoInhumadoResponse to CuerpoInhumadoDTO
            // CuerpoInhumadoDTO fastApiResponse = response.getBody();
            // CuerpoInhumadoDTO dto = new CuerpoInhumadoDTO();
            // dto.setNombre(fastApiResponse.getNombre());
            // dto.setApellido(fastApiResponse.getApellido());
            // dto.setDocumentoIdentidad(fastApiResponse.getDocumentoIdentidad());
            // dto.setNumeroProtocoloNecropsia(fastApiResponse.getNumeroProtocoloNecropsia());
            // dto.setCausaMuerte(fastApiResponse.getCausaMuerte());
            // dto.setFechaNacimiento(fastApiResponse.getFechaNacimiento());
            // dto.setFechaDefuncion(fastApiResponse.getFechaDefuncion());
            // dto.setFechaIngreso(fastApiResponse.getFechaIngreso());
            // dto.setFechaInhumacion(fastApiResponse.getFechaInhumacion());
            // dto.setFechaExhumacion(fastApiResponse.getFechaExhumacion());
            // dto.setFuncionarioReceptor(fastApiResponse.getFuncionarioReceptor());
            // dto.setCargoFuncionario(fastApiResponse.getCargoFuncionario());
            // dto.setAutoridadRemitente(fastApiResponse.getAutoridadRemitente());
            // dto.setCargoAutoridadRemitente(fastApiResponse.getCargoAutoridadRemitente());
            // dto.setAutoridadExhumacion(fastApiResponse.getAutoridadExhumacion());
            // dto.setCargoAutoridadExhumacion(fastApiResponse.getCargoAutoridadExhumacion());
            // dto.setEstado(fastApiResponse.getEstado());
            // dto.setObservaciones(fastApiResponse.getObservaciones());

            // Create the record using the service
            // CuerpoInhumadoDTO created = cuerpoInhumadoService.createCuerpoInhumado(dto);
            
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}