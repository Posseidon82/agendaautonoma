package com.agendaautonoma.interfaces.controllers;

import com.agendaautonoma.application.AgendamentoService;
import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import com.agendaautonoma.interfaces.dto.AgendamentoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AgendamentoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamentoService agendamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "cliente@email.com", roles = "CLIENTE")
    void deveCriarAgendamentoComSucesso() throws Exception {
        AgendamentoRequest request = new AgendamentoRequest();
        request.setProfissionalId(1L);
        request.setData(LocalDate.of(2026, 7, 15));
        request.setHoraInicio(LocalTime.of(14, 0));

        Agendamento mockAgendamento = Agendamento.builder()
                .id(1L)
                .profissionalId(1L)
                .clienteId(2L)
                .data(request.getData())
                .horaInicio(request.getHoraInicio())
                .horaFim(request.getHoraInicio().plusMinutes(30))
                .status(StatusAgendamento.CONFIRMADO)
                .build();

        when(agendamentoService.criarAgendamento(any())).thenReturn(mockAgendamento);

        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}