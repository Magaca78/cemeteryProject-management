package com.cementerio.cemeteryProject_management.repositories;

import com.cementerio.cemeteryProject_management.models.NichoModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INichoRepository extends JpaRepository<NichoModel, String> {
  List<NichoModel> findByEstado(NichoModel.EstadoNicho estado);
}
