package com.cementerio.cemeteryProject_management.repositories;

import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICuerpoInhumadoRepository extends JpaRepository<CuerpoInhumadoModel, String> {
  @Query("SELECT c FROM CuerpoInhumadoModel c WHERE c.idCadaver NOT IN (SELECT nc.cuerpoInhumado.idCadaver FROM NichoCuerpoModel nc)")
  List<CuerpoInhumadoModel> findCuerposNoAsignados();

  // Método para encontrar los últimos cuerpos inhumados por fecha de ingreso
  @Query("SELECT c FROM CuerpoInhumadoModel c ORDER BY c.fechaIngreso DESC")
  List<CuerpoInhumadoModel> findLatestCuerpos(Pageable pageable);

  // Método para buscar cuerpos por nombre, apellido, idCadaver o documentoIdentidad
  @Query("SELECT c FROM CuerpoInhumadoModel c WHERE " +
    "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
    "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
    "LOWER(CONCAT(c.nombre, ' ', c.apellido)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
    "LOWER(c.documentoIdentidad) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
    "LOWER(c.idCadaver) LIKE LOWER(CONCAT('%', :query, '%'))")
  List<CuerpoInhumadoModel> searchByQuery(@Param("query") String query);
}
