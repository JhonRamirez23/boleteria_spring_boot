package com.codesa.prueba.funciones.service;

import com.codesa.prueba.funciones.dto.FuncionRequest;
import com.codesa.prueba.funciones.dto.FuncionResponse;
import com.codesa.prueba.funciones.model.Funcion;
import com.codesa.prueba.funciones.repository.FuncionRepository;
import com.codesa.prueba.obras.model.Obra;
import com.codesa.prueba.obras.repository.ObraRepository;
import com.codesa.prueba.shared.exception.BusinessException;
import com.codesa.prueba.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuncionService {

    private final FuncionRepository funcionRepository;
    private final ObraRepository obraRepository;

    public Page<FuncionResponse> listarPorObra(Long obraId, boolean soloFuturas, Pageable pageable) {
        log.info("Listando funciones de obra id: {}", obraId);
        Page<Funcion> funciones = soloFuturas
                ? funcionRepository.findByObraIdAndFechaHoraAfter(obraId, LocalDateTime.now(), pageable)
                : funcionRepository.findByObraId(obraId, pageable);
        return funciones.map(this::toResponse);
    }

    public FuncionResponse obtenerPorId(Long id) {
        log.info("Obteniendo función con id: {}", id);
        return toResponse(findFuncionOrThrow(id));
    }

    @Transactional
    public FuncionResponse crear(FuncionRequest request) {
        log.info("Creando función para obra id: {}", request.getObraId());
        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Obra no encontrada con id: " + request.getObraId()));

        if (!obra.getActiva()) {
            throw new BusinessException(
                    "No se puede crear una función para una obra inactiva");
        }

        Funcion funcion = Funcion.builder()
                .obra(obra)
                .fechaHora(request.getFechaHora())
                .sala(request.getSala())
                .aforoTotal(request.getAforoTotal())
                .precio(request.getPrecio())
                .build();

        return toResponse(funcionRepository.save(funcion));
    }

    @Transactional
    public FuncionResponse actualizar(Long id, FuncionRequest request) {
        log.info("Actualizando función con id: {}", id);
        Funcion funcion = findFuncionOrThrow(id);

        Obra obra = obraRepository.findById(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Obra no encontrada con id: " + request.getObraId()));

        if (!obra.getActiva()) {
            throw new BusinessException("No se puede asignar una obra inactiva a la función");
        }

        funcion.setObra(obra);
        funcion.setFechaHora(request.getFechaHora());
        funcion.setSala(request.getSala());
        funcion.setAforoTotal(request.getAforoTotal());
        funcion.setPrecio(request.getPrecio());

        return toResponse(funcionRepository.save(funcion));
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando función con id: {}", id);
        Funcion funcion = findFuncionOrThrow(id);
        funcionRepository.delete(funcion);
    }

    public Integer calcularAforoDisponible(Long funcionId) {
        Funcion funcion = findFuncionOrThrow(funcionId);
        Integer ocupadas = funcionRepository.calcularBoletasOcupadas(funcionId);
        return funcion.getAforoTotal() - ocupadas;
    }

    private Funcion findFuncionOrThrow(Long id) {
        return funcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Función no encontrada con id: " + id));
    }

    private FuncionResponse toResponse(Funcion funcion) {
        Integer aforoDisponible = calcularAforoDisponible(funcion.getId());
        return FuncionResponse.builder()
                .id(funcion.getId())
                .obraId(funcion.getObra().getId())
                .obraTitulo(funcion.getObra().getTitulo())
                .fechaHora(funcion.getFechaHora())
                .sala(funcion.getSala())
                .aforoTotal(funcion.getAforoTotal())
                .aforoDisponible(aforoDisponible)
                .precio(funcion.getPrecio())
                .build();
    }
}
