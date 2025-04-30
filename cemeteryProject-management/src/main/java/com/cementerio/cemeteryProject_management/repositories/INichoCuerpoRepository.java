package com.cementerio.cemeteryProject_management.repositories;

import com.cementerio.cemeteryProject_management.models.NichoCuerpoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface INichoCuerpoRepository extends JpaRepository<NichoCuerpoModel, String> {
  Optional<NichoCuerpoModel> findByNicho_Codigo(String codigoNicho);
  boolean existsByNichoCodigo(String codigoNicho);
  Optional<NichoCuerpoModel> findByCuerpoInhumado_IdCadaver(String idCadaver);
}
