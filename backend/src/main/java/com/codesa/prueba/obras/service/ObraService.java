package com.codesa.prueba.obras.service;

import com.codesa.prueba.obras.dto.ObraRequest;
import com.codesa.prueba.obras.dto.ObraResponse;
import com.codesa.prueba.obras.model.Obra;
import com.codesa.prueba.obras.repository.ObraRepository;
import com.codesa.prueba.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObraService {

    private final ObraRepository obraRepository;

    public Page<ObraResponse> listarObras(Pageable pageable, boolean soloActivas) {
        log.info("Listando obras - soloActivas: {}", soloActivas);
        Page<Obra> obras = soloActivas
                ? obraRepository.findByActivaTrue(pageable)
                : obraRepository.findAll(pageable);
        return obras.map(this::toResponse);
    }

    public Page<ObraResponse> buscarPorTitulo(String titulo, Pageable pageable) {
        log.info("Buscando obras por título: {}", titulo);
        return obraRepository.buscarPorTituloNativo(titulo, pageable).map(this::toResponse);
    }

    public ObraResponse obtenerPorId(Long id) {
        log.info("Obteniendo obra con id: {}", id);
        return toResponse(findObraOrThrow(id));
    }

    @Transactional
    public ObraResponse crear(ObraRequest request) {
        log.info("Creando obra: {}", request.getTitulo());
        Obra obra = Obra.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .genero(request.getGenero())
                .duracionMinutos(request.getDuracionMinutos())
                .activa(request.getActiva() != null ? request.getActiva() : true)
                .build();
        return toResponse(obraRepository.save(obra));
    }

    @Transactional
    public ObraResponse actualizar(Long id, ObraRequest request) {
        log.info("Actualizando obra con id: {}", id);
        Obra obra = findObraOrThrow(id);
        obra.setTitulo(request.getTitulo());
        obra.setDescripcion(request.getDescripcion());
        obra.setGenero(request.getGenero());
        obra.setDuracionMinutos(request.getDuracionMinutos());
        if (request.getActiva() != null) {
            obra.setActiva(request.getActiva());
        }
        return toResponse(obraRepository.save(obra));
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando obra con id: {}", id);
        Obra obra = findObraOrThrow(id);
        obraRepository.delete(obra);
    }

    private Obra findObraOrThrow(Long id) {
        return obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Obra no encontrada con id: " + id));
    }

    private ObraResponse toResponse(Obra obra) {
        return ObraResponse.builder()
                .id(obra.getId())
                .titulo(obra.getTitulo())
                .descripcion(obra.getDescripcion())
                .genero(obra.getGenero())
                .duracionMinutos(obra.getDuracionMinutos())
                .activa(obra.getActiva())
                .build();
    }
}
