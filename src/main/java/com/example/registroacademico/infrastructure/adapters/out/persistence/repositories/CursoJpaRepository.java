package com.example.registroacademico.infrastructure.adapters.out.persistence.repositories;

import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.CursoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoJpaRepository extends JpaRepository<CursoEntity, Integer> {

    Optional<CursoEntity> findByCodigoCurso(String codigoCurso);

    // Paginación básica por periodo
    Page<CursoEntity> findByPeriodo_Id(int idPeriodo, Pageable pageable);

    // Consulta JPQL explícita para explicar @Query en clase
    @Query("SELECT c FROM CursoEntity c WHERE c.profesor.id = :idProfesor")
    Page<CursoEntity> buscarPorProfesor(@Param("idProfesor") int idProfesor, Pageable pageable);
}
