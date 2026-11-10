package com.example.registroacademico.infrastructure.adapters.out.persistence.repositories;

import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
