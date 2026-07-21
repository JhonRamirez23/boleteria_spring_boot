package com.codesa.prueba.obras.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesa.prueba.obras.model.Obra;

public interface ObraRepository extends JpaRepository<Obra, Long> {
    Page<Obra> findAll(Pageable pageable);

    Page<Obra> findByActivaTrue(Pageable pageable);

    @Query(value = "SELECT id, titulo, descripcion, genero, duracion_minutos, activa " +
            "FROM obras WHERE activa = true AND " +
            "LOWER(titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))", nativeQuery = true)
    Page<Obra> buscarPorTituloNativo(@Param("titulo") String titulo, Pageable pageable);
}
