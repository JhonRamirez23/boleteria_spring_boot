package com.codesa.prueba.funciones.controller;

import com.codesa.prueba.funciones.dto.FuncionRequest;
import com.codesa.prueba.funciones.dto.FuncionResponse;
import com.codesa.prueba.funciones.service.FuncionService;
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
@RequestMapping("/api/funciones")
@RequiredArgsConstructor
public class FuncionController {

    private final FuncionService funcionService;

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<Page<FuncionResponse>> listarPorObra(
            @PathVariable Long obraId,
            @RequestParam(defaultValue = "true") boolean soloFuturas,
            @PageableDefault(size = 10, sort = "fechaHora") Pageable pageable) {
        return ResponseEntity.ok(funcionService.listarPorObra(obraId, soloFuturas, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(funcionService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionResponse> crear(
            @Valid @RequestBody FuncionRequest request) {
        log.info("Solicitud de creación de función");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(funcionService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FuncionRequest request) {
        log.info("Solicitud de actualización de función con id: {}", id);
        return ResponseEntity.ok(funcionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Solicitud de eliminación de función con id: {}", id);
        funcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
