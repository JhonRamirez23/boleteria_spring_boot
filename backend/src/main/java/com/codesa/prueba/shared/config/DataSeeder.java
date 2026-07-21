package com.codesa.prueba.shared.config;

import com.codesa.prueba.auth.model.Rol;
import com.codesa.prueba.auth.model.Usuario;
import com.codesa.prueba.auth.repository.UsuarioRepository;
import com.codesa.prueba.auth.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        seedUsuarios();
    }

    private void seedUsuarios() {
        crearUsuarioSiNoExiste("admin@codesa.com", "Admin1234", "ADMIN", "Administrador");
        crearUsuarioSiNoExiste("user@codesa.com", "User1234", "USER", "Usuario Demo");
    }

    private void crearUsuarioSiNoExiste(String email, String password,
                                         String nombreRol, String nombre) {
        if (usuarioRepository.existsByEmail(email)) {
            log.info("Usuario ya existe, omitiendo: {}", email);
            return;
        }

        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));

        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .email(email)
                .password(passwordEncoder.encode(password))
                .rol(rol)
                .build();

        usuarioRepository.save(usuario);
        log.info("Usuario creado: {} con rol {}", email, nombreRol);
    }
}