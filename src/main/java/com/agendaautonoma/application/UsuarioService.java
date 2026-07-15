package com.agendaautonoma.application;

import com.agendaautonoma.domain.Usuario;
import com.agendaautonoma.domain.enums.PapelUsuario;
import com.agendaautonoma.infrastructure.persistence.UsuarioRepository;
import com.agendaautonoma.interfaces.dto.HorarioTrabalhoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return buscarPorEmail(email);
    }

    @Transactional
    public Usuario atualizarHorarios(HorarioTrabalhoRequest request) {
        Usuario profissional = getUsuarioLogado();
        if (profissional.getPapel() != PapelUsuario.PROFISSIONAL) {
            throw new RuntimeException("Apenas profissionais podem definir horários de trabalho");
        }
        profissional.setDiasTrabalho(request.getDiasTrabalho());
        profissional.setInicioTrabalho(request.getInicioTrabalho());
        profissional.setFimTrabalho(request.getFimTrabalho());
        profissional.setDuracaoPadraoMinutos(request.getDuracaoPadraoMinutos());
        return usuarioRepository.save(profissional);
    }
}