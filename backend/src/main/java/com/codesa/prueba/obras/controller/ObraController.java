package com.codesa.prueba.obras.controller;

import com.codesa.prueba.obras.dto.ObraRequest;
import com.codesa.prueba.obras.dto.ObraResponse;
import com.codesa.prueba.obras.service.ObraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;

    @GetMapping
    public ResponseEntity<Page<ObraResponse>> listar(
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable,
            @RequestParam(required = false) String titulo) {
        if (titulo != null && !titulo.isBlank()) {
            return ResponseEntity.ok(obraService.buscarPorTitulo(titulo, pageable));
        }
        return ResponseEntity.ok(obraService.listarObras(pageable, true));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ObraResponse>> listarAdmin(
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(obraService.listarObras(pageable, false));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObraResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(obraService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObraResponse> crear(@Valid @RequestBody ObraRequest request) {
        log.info("Solicitud de creación de obra");
        return ResponseEntity.status(HttpStatus.CREATED).body(obraService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObraResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody ObraRequest request) {
        log.info("Solicitud de actualización de obra con id: {}", id);
        return ResponseEntity.ok(obraService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Solicitud de eliminación de obra con id: {}", id);
        obraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
