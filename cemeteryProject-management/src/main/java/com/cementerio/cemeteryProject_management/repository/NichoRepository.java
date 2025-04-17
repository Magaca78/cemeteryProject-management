package com.cementerio.cemeteryProject_management.repository;

import com.cementerio.cemeteryProject_management.model.Nicho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NichoRepository extends JpaRepository<Nicho, String> {
    // Puedes agregar métodos personalizados si lo necesitas
}
