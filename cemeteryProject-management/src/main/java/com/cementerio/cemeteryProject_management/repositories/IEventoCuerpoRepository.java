package com.cementerio.cemeteryProject_management.repositories;

import com.cementerio.cemeteryProject_management.models.EventoCuerpoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IEventoCuerpoRepository extends JpaRepository<EventoCuerpoModel, String> {
    List<EventoCuerpoModel> findByCuerpoInhumado_IdCadaver(String idCadaver);
    List<EventoCuerpoModel> findByTipoEventoIgnoreCase(String tipoEvento);
    List<EventoCuerpoModel> findByFechaEventoBetween(LocalDate inicio, LocalDate fin);

    @Query("SELECT e FROM EventoCuerpoModel e ORDER BY e.fechaEvento DESC")
    List<EventoCuerpoModel> findLatestEventos(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT e FROM EventoCuerpoModel e WHERE LOWER(e.resumenEvento) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<EventoCuerpoModel> searchByResumen(@Param("query") String query);
}

