package com.simbora.simbora_api.ServiceTests;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.service.EventoService;
import com.simbora.simbora_api.model.entity.Evento;
import com.simbora.simbora_api.model.entity.Organizador;
import com.simbora.simbora_api.model.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    private EventoService service;

    @BeforeEach
    void setUp() {
        service = new EventoService(eventoRepository);
    }

    // ---------- métodos auxiliares ----------

    /**
     * Evento-base (caso nominal, com valores mínimos válidos): nome, descrição,
     * local, cidade e status com 1 caractere ("A"), dataInicio informada,
     * capacidade 1 e organizador com id 1.
     */
    private Evento criarEventoBase() {
        Organizador organizador = new Organizador();
        organizador.setId(1L);

        Evento evento = new Evento();
        evento.setNome("A");
        evento.setDescricao("A");
        evento.setDataInicio(LocalDateTime.of(2026, 12, 20, 20, 0, 0));
        evento.setLocal("A");
        evento.setCidade("A");
        evento.setCapacidadeMaxima(1);
        evento.setStatus("A");
        evento.setOrganizador(organizador);
        return evento;
    }

    private void assertRegra(String mensagemEsperada, Evento evento) {
        RegraNegocioException ex = assertThrows(
                RegraNegocioException.class,
                () -> service.validar(evento));
        assertEquals(mensagemEsperada, ex.getMessage());
    }

    // ---------- casos de teste ----------

    @Test
    @DisplayName("E01 - evento-base válido (valores mínimos)")
    void e01_eventoBaseValido() {
        Evento evento = criarEventoBase();

        assertDoesNotThrow(() -> service.validar(evento));
        verifyNoInteractions(eventoRepository);
    }

    @Test
    @DisplayName("E02 - nome nulo")
    void e02_nomeNulo() {
        Evento evento = criarEventoBase();
        evento.setNome(null);

        assertRegra("Título inválido", evento);
    }

    @Test
    @DisplayName("E03 - nome vazio")
    void e03_nomeVazio() {
        Evento evento = criarEventoBase();
        evento.setNome("");

        assertRegra("Título inválido", evento);
    }

    @Test
    @DisplayName("E04 - descrição nula")
    void e04_descricaoNula() {
        Evento evento = criarEventoBase();
        evento.setDescricao(null);

        assertRegra("Descrição inválida", evento);
    }

    @Test
    @DisplayName("E05 - descrição vazia")
    void e05_descricaoVazia() {
        Evento evento = criarEventoBase();
        evento.setDescricao("");

        assertRegra("Descrição inválida", evento);
    }

    @Test
    @DisplayName("E06 - data de início nula")
    void e06_dataInicioNula() {
        Evento evento = criarEventoBase();
        evento.setDataInicio(null);

        assertRegra("Data de início inválida", evento);
    }

    @Test
    @DisplayName("E07 - local nulo")
    void e07_localNulo() {
        Evento evento = criarEventoBase();
        evento.setLocal(null);

        assertRegra("Local inválido", evento);
    }

    @Test
    @DisplayName("E08 - local vazio")
    void e08_localVazio() {
        Evento evento = criarEventoBase();
        evento.setLocal("");

        assertRegra("Local inválido", evento);
    }

    @Test
    @DisplayName("E09 - cidade nula")
    void e09_cidadeNula() {
        Evento evento = criarEventoBase();
        evento.setCidade(null);

        assertRegra("Cidade inválida", evento);
    }

    @Test
    @DisplayName("E10 - capacidade máxima nula")
    void e10_capacidadeNula() {
        Evento evento = criarEventoBase();
        evento.setCapacidadeMaxima(null);

        assertRegra("Capacidade máxima inválida", evento);
    }

    @Test
    @DisplayName("E11 - capacidade máxima igual a 0 (limite inferior - 1)")
    void e11_capacidadeZero() {
        Evento evento = criarEventoBase();
        evento.setCapacidadeMaxima(0);

        assertRegra("Capacidade máxima inválida", evento);
    }

    @Test
    @DisplayName("E12 - status nulo")
    void e12_statusNulo() {
        Evento evento = criarEventoBase();
        evento.setStatus(null);

        assertRegra("Status inválido", evento);
    }

    @Test
    @DisplayName("E13 - status vazio")
    void e13_statusVazio() {
        Evento evento = criarEventoBase();
        evento.setStatus("");

        assertRegra("Status inválido", evento);
    }

    @Test
    @DisplayName("E14 - organizador nulo")
    void e14_organizadorNulo() {
        Evento evento = criarEventoBase();
        evento.setOrganizador(null);

        assertRegra("Organizador inválido", evento);
    }

    @Test
    @DisplayName("E15 - id do organizador nulo")
    void e15_idDoOrganizadorNulo() {
        Evento evento = criarEventoBase();
        evento.getOrganizador().setId(null);

        assertRegra("Organizador inválido", evento);
    }
}