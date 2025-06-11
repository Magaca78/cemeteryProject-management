package com.cementerio.cemeteryProject_management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CuerpoInhumadoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Para parsear JSON

    @Test
    void testCrearYRecuperarCuerpoInhumado() throws Exception {
        // Crear un cuerpo inhumado con un JSON válido (solo un objeto)
        String cuerpoPayload = """
                    {
                        "nombre": "María",
                        "apellido": "González",
                        "documentoIdentidad": "987654321",
                        "numeroProtocoloNecropsia": "NP-045",
                        "causaMuerte": "Neumonía grave",
                        "fechaNacimiento": "1945-08-22",
                        "fechaDefuncion": "2023-10-15",
                        "fechaIngreso": "2023-10-16T09:45:00",
                        "fechaInhumacion": "2023-10-17",
                        "fechaExhumacion": "2025-03-01",
                        "funcionarioReceptor": "Luis Martínez",
                        "cargoFuncionario": "Encargado de registro",
                        "autoridadRemitente": "Clínica San Rafael",
                        "cargoAutoridadRemitente": "Médico Forense",
                        "autoridadExhumacion": "Fiscalía General",
                        "cargoAutoridadExhumacion": "Fiscal Delegado",
                        "estado": "EXHUMADO",
                        "observaciones": "Exhumación realizada por orden judicial."
                    }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(cuerpoPayload, headers);

        // Enviar el POST
        ResponseEntity<String> crearResponse = restTemplate.postForEntity(
                "/cuerposinhumados",
                request,
                String.class);

        // Validar respuesta
        assertEquals(200, crearResponse.getStatusCodeValue());
        assertNotNull(crearResponse.getBody());

        // Obtener el ID del cuerpo
        JsonNode responseJson = objectMapper.readTree(crearResponse.getBody());
        String idCadaver = responseJson.get("idCadaver").asText();
        assertNotNull(idCadaver);

        // Recuperar el cuerpo por ID
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                "/cuerposinhumados/" + idCadaver,
                String.class);

        assertEquals(200, getResponse.getStatusCodeValue());
        assertTrue(getResponse.getBody().contains("María"));
        assertTrue(getResponse.getBody().contains("González"));
    }

    @Test
    void testListarTodosCuerposInhumados() throws Exception {
        // Crear dos cuerpos inhumados con todos los atributos
        String cuerpo1Payload = """
                {
                               "nombre": "Juan",
                               "apellido": "González",
                               "documentoIdentidad": "987654321",
                               "numeroProtocoloNecropsia": "NP-045",
                               "causaMuerte": "Neumonía grave",
                               "fechaNacimiento": "1945-08-22",
                               "fechaDefuncion": "2023-10-15",
                               "fechaIngreso": "2023-10-16T09:45:00",
                               "fechaInhumacion": "2023-10-17",
                               "fechaExhumacion": "2025-03-01",
                               "funcionarioReceptor": "Luis Martínez",
                               "cargoFuncionario": "Encargado de registro",
                               "autoridadRemitente": "Clínica San Rafael",
                               "cargoAutoridadRemitente": "Médico Forense",
                               "autoridadExhumacion": "Fiscalía General",
                               "cargoAutoridadExhumacion": "Fiscal Delegado",
                               "estado": "EXHUMADO",
                               "observaciones": "Exhumación realizada por orden judicial."
                           }
                   """;
        String cuerpo2Payload = """
                {
                        "nombre": "Ana",
                        "apellido": "González",
                        "documentoIdentidad": "85545665",
                        "numeroProtocoloNecropsia": "NP-045",
                        "causaMuerte": "Neumonía grave",
                        "fechaNacimiento": "1945-08-22",
                        "fechaDefuncion": "2023-10-15",
                        "fechaIngreso": "2023-10-16T09:45:00",
                        "fechaInhumacion": "2023-10-17",
                        "fechaExhumacion": "2025-03-01",
                        "funcionarioReceptor": "Luis Martínez",
                        "cargoFuncionario": "Encargado de registro",
                        "autoridadRemitente": "Clínica San Rafael",
                        "cargoAutoridadRemitente": "Médico Forense",
                        "autoridadExhumacion": "Fiscalía General",
                        "cargoAutoridadExhumacion": "Fiscal Delegado",
                        "estado": "INHUMADO",
                        "observaciones": "Exhumación realizada por orden judicial."
                    }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request1 = new HttpEntity<>(cuerpo1Payload, headers);
        HttpEntity<String> request2 = new HttpEntity<>(cuerpo2Payload, headers);

        restTemplate.postForEntity("/cuerposinhumados", request1, String.class);
        restTemplate.postForEntity("/cuerposinhumados", request2, String.class);

        // Obtener la lista de todos los cuerpos
        ResponseEntity<String> getAllResponse = restTemplate.getForEntity("/cuerposinhumados", String.class);
        assertEquals(200, getAllResponse.getStatusCodeValue());
        JsonNode responseJson = objectMapper.readTree(getAllResponse.getBody());
        assertTrue(responseJson.isArray());
        assertEquals(2, responseJson.size()); // Verifica que haya 2 cuerpos
        assertTrue(responseJson.toString().contains("Juan"));
        assertTrue(responseJson.toString().contains("Ana"));
    }

    @Test
    void testActualizarCuerpoInhumado() throws Exception {
        // Crear un cuerpo inhumado con todos los atributos
        String cuerpoPayload = """
                {
                    "nombre": "María",
                    "apellido": "González",
                    "documentoIdentidad": "987654321",
                    "numeroProtocoloNecropsia": "NP-045",
                    "causaMuerte": "Neumonía grave",
                    "fechaNacimiento": "1945-08-22",
                    "fechaDefuncion": "2023-10-15",
                    "fechaIngreso": "2023-10-16T09:45:00",
                    "fechaInhumacion": "2023-10-17",
                    "fechaExhumacion": "2025-03-01",
                    "funcionarioReceptor": "Luis Martínez",
                    "cargoFuncionario": "Encargado de registro",
                    "autoridadRemitente": "Clínica San Rafael",
                    "cargoAutoridadRemitente": "Médico Forense",
                    "autoridadExhumacion": "Fiscalía General",
                    "cargoAutoridadExhumacion": "Fiscal Delegado",
                    "estado": "EXHUMADO",
                    "observaciones": "Exhumación realizada por orden judicial."
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(cuerpoPayload, headers);
        ResponseEntity<String> crearResponse = restTemplate.postForEntity("/cuerposinhumados", request, String.class);
        assertEquals(200, crearResponse.getStatusCodeValue());
        JsonNode responseJson = objectMapper.readTree(crearResponse.getBody());
        String idCadaver = responseJson.get("idCadaver").asText();

        // Actualizar el cuerpo con todos los atributos
        String updatePayload = """
                {
                    "nombre": "María Actualizada",
                    "apellido": "González",
                    "documentoIdentidad": "987654321",
                    "numeroProtocoloNecropsia": "NP-045",
                    "causaMuerte": "Neumonía grave",
                    "fechaNacimiento": "1945-08-22",
                    "fechaDefuncion": "2023-10-15",
                    "fechaIngreso": "2023-10-16T09:45:00",
                    "fechaInhumacion": "2023-10-17",
                    "fechaExhumacion": "2025-03-01",
                    "funcionarioReceptor": "Luis Martínez",
                    "cargoFuncionario": "Encargado de registro",
                    "autoridadRemitente": "Clínica San Rafael",
                    "cargoAutoridadRemitente": "Médico Forense",
                    "autoridadExhumacion": "Fiscalía General",
                    "cargoAutoridadExhumacion": "Fiscal Delegado",
                    "estado": "EXHUMADO",
                    "observaciones": "Exhumación realizada por orden judicial. Actualizado."
                }
                """;
        HttpEntity<String> updateRequest = new HttpEntity<>(updatePayload, headers);
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                "/cuerposinhumados/" + idCadaver,
                HttpMethod.PUT,
                updateRequest,
                String.class);
        assertEquals(200, updateResponse.getStatusCodeValue());

        // Recuperar y verificar
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/cuerposinhumados/" + idCadaver, String.class);
        assertEquals(200, getResponse.getStatusCodeValue());
        JsonNode getResponseJson = objectMapper.readTree(getResponse.getBody());
        assertEquals("María Actualizada", getResponseJson.get("nombre").asText());
    }

    @Test
    void testEliminarCuerpoInhumado() throws Exception {
        // Crear un cuerpo inhumado con todos los atributos
        String cuerpoPayload = """
                {
                    "nombre": "Pedro",
                    "apellido": "López",
                    "documentoIdentidad": "456789123",
                    "numeroProtocoloNecropsia": "NP-003",
                    "causaMuerte": "Causas naturales",
                    "fechaNacimiento": "1960-01-12",
                    "fechaDefuncion": "2023-10-18",
                    "fechaIngreso": "2023-10-18T14:00:00",
                    "fechaInhumacion": "2023-10-19",
                    "fechaExhumacion": "2023-10-17",
                    "funcionarioReceptor": "Ana Torres",
                    "cargoFuncionario": "Encargada de registro",
                    "autoridadRemitente": "Hospital Central",
                    "cargoAutoridadRemitente": "Médico Forense",
                    "autoridadExhumacion": "Policia",
                    "cargoAutoridadExhumacion": "Sargento",
                    "estado": "INHUMADO",
                    "observaciones": "Sin observaciones."
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(cuerpoPayload, headers);
        ResponseEntity<String> crearResponse = restTemplate.postForEntity("/cuerposinhumados", request, String.class);
        assertEquals(200, crearResponse.getStatusCodeValue());
        JsonNode responseJson = objectMapper.readTree(crearResponse.getBody());
        String idCadaver = responseJson.get("idCadaver").asText();

        // Eliminar el cuerpo
        restTemplate.delete("/cuerposinhumados/" + idCadaver);
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/cuerposinhumados/" + idCadaver, String.class);
        assertEquals(404, getResponse.getStatusCodeValue());
    }

    @Test
    void testBuscarCuerpos() throws Exception {
        // Crear dos cuerpos inhumados con todos los atributos
        String cuerpo1Payload = """
            {
                "nombre": "Juan",
                "apellido": "Pérez",
                "documentoIdentidad": "123456789",
                "numeroProtocoloNecropsia": "NP-001",
                "causaMuerte": "Infarto",
                "fechaNacimiento": "1950-05-10",
                "fechaDefuncion": "2023-10-18",
                "fechaIngreso": "2023-10-18T10:00:00",
                "fechaInhumacion": "2023-10-19",
                "fechaExhumacion": "2023-10-25",
                "funcionarioReceptor": "Carlos Gómez",
                "cargoFuncionario": "Encargado de registro",
                "autoridadRemitente": "Hospital General",
                "cargoAutoridadRemitente": "Médico Forense",
                "autoridadExhumacion": "Policia",
                "cargoAutoridadExhumacion": "Capitan",
                "estado": "INHUMADO",
                "observaciones": "Sin observaciones."
            }
            """;
        String cuerpo2Payload = """
            {
                "nombre": "Ana",
                "apellido": "Pérez",
                "documentoIdentidad": "987654321",
                "numeroProtocoloNecropsia": "NP-002",
                "causaMuerte": "Accidente",
                "fechaNacimiento": "1975-03-15",
                "fechaDefuncion": "2023-10-19",
                "fechaIngreso": "2023-10-19T11:30:00",
                "fechaInhumacion": "2023-10-20",
                "fechaExhumacion": "2023-10-25",
                "funcionarioReceptor": "María López",
                "cargoFuncionario": "Asistente de registro",
                "autoridadRemitente": "Clínica del Norte",
                "cargoAutoridadRemitente": "Médico Forense",
                "autoridadExhumacion": "Fiscalia",
                "cargoAutoridadExhumacion": "Fiscal",
                "estado": "INHUMADO",
                "observaciones": "Pendiente de asignación."
            }
            """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request1 = new HttpEntity<>(cuerpo1Payload, headers);
        HttpEntity<String> request2 = new HttpEntity<>(cuerpo2Payload, headers);

        restTemplate.postForEntity("/cuerposinhumados", request1, String.class);
        restTemplate.postForEntity("/cuerposinhumados", request2, String.class);

        // Buscar cuerpos con el término "Pérez"
        ResponseEntity<String> searchResponse = restTemplate.getForEntity("/cuerposinhumados/search?query=Pérez", String.class);
        assertEquals(200, searchResponse.getStatusCodeValue());
        JsonNode responseJson = objectMapper.readTree(searchResponse.getBody());
        assertTrue(responseJson.isArray());
        assertEquals(2, responseJson.size()); // Ambos tienen "Pérez"
        assertTrue(responseJson.toString().contains("Juan"));
        assertTrue(responseJson.toString().contains("Ana"));
    }
    }
