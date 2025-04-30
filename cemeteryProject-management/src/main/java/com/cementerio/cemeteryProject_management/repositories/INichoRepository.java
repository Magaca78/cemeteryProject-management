package com.cementerio.cemeteryProject_management.repositories;

import com.cementerio.cemeteryProject_management.models.NichoModel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface INichoRepository extends JpaRepository<NichoModel, String> {
  List<NichoModel> findByEstado(NichoModel.EstadoNicho estado);

  @Query("SELECT n FROM NichoModel n WHERE UPPER(n.codigo) = UPPER(:codigo)")
  Optional<NichoModel> findByCodigoIgnoreCase(@Param("codigo") String codigo);
}
