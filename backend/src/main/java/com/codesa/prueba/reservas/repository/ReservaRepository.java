package com.codesa.prueba.reservas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codesa.prueba.reservas.model.EstadoReserva;
import com.codesa.prueba.reservas.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Page<Reserva> findByUsuarioId(Long usuarioId, Pageable pageable);

    Page<Reserva> findByFuncionId(Long funcionId, Pageable pageable);

    Page<Reserva> findByEstado(EstadoReserva estado, Pageable pageable);

    boolean existsByUsuarioIdAndFuncionIdAndEstadoIn(Long usuarioId, Long funcionId, java.util.List<EstadoReserva> estados);
}
