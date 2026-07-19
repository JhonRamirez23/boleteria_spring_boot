package com.codesa.prueba.reservas.service;

import com.codesa.prueba.auth.model.Usuario;
import com.codesa.prueba.auth.repository.UsuarioRepository;
import com.codesa.prueba.funciones.model.Funcion;
import com.codesa.prueba.funciones.repository.FuncionRepository;
import com.codesa.prueba.reservas.dto.ReservaRequest;
import com.codesa.prueba.reservas.dto.ReservaResponse;
import com.codesa.prueba.reservas.model.EstadoReserva;
import com.codesa.prueba.reservas.model.Reserva;
import com.codesa.prueba.reservas.repository.ReservaRepository;
import com.codesa.prueba.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final FuncionRepository funcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaValidacionService validacionService;

    @Transactional
    public ReservaResponse crear(ReservaRequest request, String email) {
        log.info("Creando reserva para usuario: {} en función: {}",
                email, request.getFuncionId());

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + email));

        Funcion funcion = funcionRepository.findById(request.getFuncionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Función no encontrada con id: " + request.getFuncionId()));

        validacionService.validarReservaduplicada(usuario.getId(), funcion.getId());
        validacionService.validarAforo(funcion.getId(), request.getCantidad());

        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .funcion(funcion)
                .cantidad(request.getCantidad())
                .estado(EstadoReserva.PENDIENTE)
                .build();

        return toResponse(reservaRepository.save(reserva));
    }

    @Transactional(readOnly = true)
    public Page<ReservaResponse> listarMisReservas(String email, Pageable pageable) {
        log.info("Listando reservas del usuario: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + email));
        return reservaRepository.findByUsuarioId(usuario.getId(), pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReservaResponse> listarPorFuncion(Long funcionId, Pageable pageable) {
        log.info("Listando reservas de función id: {}", funcionId);
        return reservaRepository.findByFuncionId(funcionId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReservaResponse> listarPorEstado(EstadoReserva estado, Pageable pageable) {
        log.info("Listando reservas con estado: {}", estado);
        return reservaRepository.findByEstado(estado, pageable)
                .map(this::toResponse);
    }

    public Reserva findReservaOrThrow(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con id: " + id));
    }

    public ReservaResponse toResponse(Reserva reserva) {
        return ReservaResponse.builder()
                .id(reserva.getId())
                .funcionId(reserva.getFuncion().getId())
                .obraTitulo(reserva.getFuncion().getObra().getTitulo())
                .sala(reserva.getFuncion().getSala())
                .fechaFuncion(reserva.getFuncion().getFechaHora())
                .cantidad(reserva.getCantidad())
                .precioUnitario(reserva.getFuncion().getPrecio())
                .total(reserva.getFuncion().getPrecio()
                        .multiply(BigDecimal.valueOf(reserva.getCantidad())))
                .estado(reserva.getEstado())
                .fechaReserva(reserva.getFechaReserva())
                .usuarioEmail(reserva.getUsuario().getEmail())
                .build();
    }
}
