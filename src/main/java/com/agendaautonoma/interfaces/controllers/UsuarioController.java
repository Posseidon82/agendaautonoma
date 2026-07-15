package com.agendaautonoma.interfaces.controllers;

import com.agendaautonoma.application.UsuarioService;
import com.agendaautonoma.domain.Usuario;
import com.agendaautonoma.interfaces.dto.HorarioTrabalhoRequest;
import com.agendaautonoma.interfaces.dto.HorarioTrabalhoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PutMapping("/horarios-trabalho")
    public ResponseEntity<HorarioTrabalhoResponse> atualizarHorarios(
            @Valid @RequestBody HorarioTrabalhoRequest request) {
        Usuario profissional = usuarioService.atualizarHorarios(request);
        return ResponseEntity.ok(new HorarioTrabalhoResponse(profissional));
    }

    @GetMapping("/horarios-trabalho")
    public ResponseEntity<HorarioTrabalhoResponse> obterHorarios() {
        Usuario profissional = usuarioService.getUsuarioLogado();
        return ResponseEntity.ok(new HorarioTrabalhoResponse(profissional));
    }
}