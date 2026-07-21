package com.codesa.prueba.funciones.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesa.prueba.funciones.model.Funcion;

public interface FuncionRepository  extends JpaRepository<Funcion, Long> {
    Page<Funcion> findByObraId(Long obraId, Pageable pageable);

    Page<Funcion> findByObraIdAndFechaHoraAfter(Long obraId, java.time.LocalDateTime fecha, Pageable pageable);

    @Query(value = """
            SELECT COALESCE(SUM(r.cantidad), 0)
            FROM reservas r
            WHERE r.funcion_id = :funcionId
            AND r.estado IN ('PENDIENTE', 'CONFIRMADA')
            """, nativeQuery = true)
    Integer calcularBoletasOcupadas(@Param("funcionId") Long funcionId);
}
