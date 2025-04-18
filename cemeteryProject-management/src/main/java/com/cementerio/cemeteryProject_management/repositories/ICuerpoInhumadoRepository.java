package com.cementerio.cemeteryProject_management.repositories;

import com.cementerio.cemeteryProject_management.models.CuerpoInhumadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICuerpoInhumadoRepository extends JpaRepository<CuerpoInhumadoModel, String> {
}
