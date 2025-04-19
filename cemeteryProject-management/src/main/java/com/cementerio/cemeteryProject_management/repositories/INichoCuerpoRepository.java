package com.cementerio.cemeteryProject_management.repositories;

import com.cementerio.cemeteryProject_management.models.NichoCuerpoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INichoCuerpoRepository extends JpaRepository<NichoCuerpoModel, String> {
}
