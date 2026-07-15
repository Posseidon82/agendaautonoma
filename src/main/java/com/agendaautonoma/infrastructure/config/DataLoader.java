package com.agendaautonoma.infrastructure.config;

import com.agendaautonoma.application.UsuarioService;
import com.agendaautonoma.domain.Usuario;
import com.agendaautonoma.domain.enums.PapelUsuario;
import com.agendaautonoma.infrastructure.persistence.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository; // Adicionado

    @Override
    public void run(String... args) {
        // Criar profissional padrão se não existir
        if (!usuarioRepository.existsByEmail("prof@email.com")) {
            Usuario profissional = Usuario.builder()
                    .nome("Dr. João Silva")
                    .email("prof@email.com")
                    .senha("123456") // será codificada no service
                    .papel(PapelUsuario.PROFISSIONAL)
                    .diasTrabalho("SEG,TER,QUA,QUI,SEX")
                    .inicioTrabalho(LocalTime.of(9, 0))
                    .fimTrabalho(LocalTime.of(18, 0))
                    .duracaoPadraoMinutos(30)
                    .build();
            usuarioService.registrar(profissional);
        }

        // Criar cliente padrão se não existir
        if (!usuarioRepository.existsByEmail("cliente@email.com")) {
            Usuario cliente = Usuario.builder()
                    .nome("Maria Santos")
                    .email("cliente@email.com")
                    .senha("123456")
                    .papel(PapelUsuario.CLIENTE)
                    .build();
            usuarioService.registrar(cliente);
        }
    }
}