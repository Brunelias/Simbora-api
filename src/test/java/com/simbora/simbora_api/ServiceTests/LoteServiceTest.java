package com.simbora.simbora_api.ServiceTests;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.service.LoteService;
import com.simbora.simbora_api.model.entity.Evento;
import com.simbora.simbora_api.model.entity.Lote;
import com.simbora.simbora_api.model.repository.LoteRepository;
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
class LoteServiceTest {

    private static final LocalDateTime DATA_INICIO = LocalDateTime.of(2026, 12, 1, 10, 0, 0);

    @Mock
    private LoteRepository loteRepository;

    private LoteService service;

    @BeforeEach
    void setUp() {
        service = new LoteService(loteRepository);
    }

    // ---------- métodos auxiliares ----------

    /**
     * Lote-base (caso nominal, com valores mínimos válidos): nome "A",
     * dataFim = dataInicio + 1 segundo, quantidade 1, pago, preço 0,01, evento id 1.
     */
    private Lote criarLoteBase() {
        Evento evento = new Evento();
        evento.setId(1L);

        Lote lote = new Lote();
        lote.setNome("A");
        lote.setDataInicio(DATA_INICIO);
        lote.setDataFim(DATA_INICIO.plusSeconds(1));
        lote.setQuantidade(1);
        lote.setGratuito(false);
        lote.setPrecoUnitario(0.01);
        lote.setEvento(evento);
        return lote;
    }

    private void assertRegra(String mensagemEsperada, Lote lote) {
        RegraNegocioException ex = assertThrows(
                RegraNegocioException.class,
                () -> service.validar(lote));
        assertEquals(mensagemEsperada, ex.getMessage());
    }

    // ---------- casos de teste ----------

    @Test
    @DisplayName("L01 - lote-base válido (valores mínimos)")
    void l01_loteBaseValido() {
        Lote lote = criarLoteBase();

        assertDoesNotThrow(() -> service.validar(lote));
        verifyNoInteractions(loteRepository);
    }

    @Test
    @DisplayName("L02 - nome nulo")
    void l02_nomeNulo() {
        Lote lote = criarLoteBase();
        lote.setNome(null);

        assertRegra("Nome inválido", lote);
    }

    @Test
    @DisplayName("L03 - nome vazio")
    void l03_nomeVazio() {
        Lote lote = criarLoteBase();
        lote.setNome("");

        assertRegra("Nome inválido", lote);
    }

    @Test
    @DisplayName("L04 - data de início nula")
    void l04_dataInicioNula() {
        Lote lote = criarLoteBase();
        lote.setDataInicio(null);

        assertRegra("Data de início inválida", lote);
    }

    @Test
    @DisplayName("L05 - data de fim nula")
    void l05_dataFimNula() {
        Lote lote = criarLoteBase();
        lote.setDataFim(null);

        assertRegra("Data de fim inválida", lote);
    }

    @Test
    @DisplayName("L06 - data de fim igual à data de início (limite)")
    void l06_dataFimIgualDataInicio() {
        Lote lote = criarLoteBase();
        lote.setDataFim(DATA_INICIO);

        assertRegra("A data de fim deve ser posterior à data de início", lote);
    }

    @Test
    @DisplayName("L07 - quantidade nula")
    void l07_quantidadeNula() {
        Lote lote = criarLoteBase();
        lote.setQuantidade(null);

        assertRegra("Quantidade inválida", lote);
    }

    @Test
    @DisplayName("L08 - quantidade igual a 0 (limite inferior - 1)")
    void l08_quantidadeZero() {
        Lote lote = criarLoteBase();
        lote.setQuantidade(0);

        assertRegra("Quantidade inválida", lote);
    }

    @Test
    @DisplayName("L09 - campo gratuito nulo")
    void l09_gratuitoNulo() {
        Lote lote = criarLoteBase();
        lote.setGratuito(null);

        assertRegra("Informe se o lote é gratuito ou pago", lote);
    }

    @Test
    @DisplayName("L10 - preço unitário nulo")
    void l10_precoNulo() {
        Lote lote = criarLoteBase();
        lote.setPrecoUnitario(null);

        assertRegra("Preço unitário inválido", lote);
    }

    @Test
    @DisplayName("L11 - lote gratuito com preço 0,01 (limite zero + 1 centavo)")
    void l11_gratuitoComPrecoDiferenteDeZero() {
        Lote lote = criarLoteBase();
        lote.setGratuito(true);
        lote.setPrecoUnitario(0.01);

        assertRegra("Lote gratuito deve ter preço zero", lote);
    }

    @Test
    @DisplayName("L12 - lote gratuito com preço 0,00 (válido)")
    void l12_gratuitoComPrecoZero() {
        Lote lote = criarLoteBase();
        lote.setGratuito(true);
        lote.setPrecoUnitario(0.0);

        assertDoesNotThrow(() -> service.validar(lote));
    }

    @Test
    @DisplayName("L13 - lote pago com preço 0,00 (limite)")
    void l13_pagoComPrecoZero() {
        Lote lote = criarLoteBase();
        lote.setGratuito(false);
        lote.setPrecoUnitario(0.0);

        assertRegra("Lote pago deve ter preço maior que zero", lote);
    }

    @Test
    @DisplayName("L14 - evento nulo")
    void l14_eventoNulo() {
        Lote lote = criarLoteBase();
        lote.setEvento(null);

        assertRegra("Evento inválido", lote);
    }

    @Test
    @DisplayName("L15 - id do evento nulo")
    void l15_idDoEventoNulo() {
        Lote lote = criarLoteBase();
        lote.getEvento().setId(null);

        assertRegra("Evento inválido", lote);
    }
}