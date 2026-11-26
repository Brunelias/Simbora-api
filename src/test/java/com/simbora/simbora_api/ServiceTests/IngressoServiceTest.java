package com.simbora.simbora_api.ServiceTests;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.service.IngressoService;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.model.entity.Ingresso;
import com.simbora.simbora_api.model.entity.ItemCompra;
import com.simbora.simbora_api.model.entity.Lote;
import com.simbora.simbora_api.model.repository.IngressoRepository;
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
class IngressoServiceTest {

    @Mock
    private IngressoRepository ingressoRepository;

    private IngressoService service;

    @BeforeEach
    void setUp() {
        service = new IngressoService(ingressoRepository);
    }

    // ---------- métodos auxiliares ----------

    /**
     * Ingresso-base (caso nominal, com valores mínimos válidos): dataGeracao
     * informada, código e status com 1 caractere ("A") e lote, cliente e
     * item de compra com id 1.
     */
    private Ingresso criarIngressoBase() {
        Lote lote = new Lote();
        lote.setId(1L);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        ItemCompra itemCompra = new ItemCompra();
        itemCompra.setId(1L);

        Ingresso ingresso = new Ingresso();
        ingresso.setDataGeracao(LocalDateTime.of(2026, 12, 1, 10, 0, 0));
        ingresso.setCodigo("A");
        ingresso.setStatus("A");
        ingresso.setLote(lote);
        ingresso.setCliente(cliente);
        ingresso.setItemCompra(itemCompra);
        return ingresso;
    }

    private void assertRegra(String mensagemEsperada, Ingresso ingresso) {
        RegraNegocioException ex = assertThrows(
                RegraNegocioException.class,
                () -> service.validar(ingresso));
        assertEquals(mensagemEsperada, ex.getMessage());
    }

    // ---------- casos de teste ----------

    @Test
    @DisplayName("I01 - ingresso-base válido (valores mínimos)")
    void i01_ingressoBaseValido() {
        Ingresso ingresso = criarIngressoBase();

        assertDoesNotThrow(() -> service.validar(ingresso));
        verifyNoInteractions(ingressoRepository);
    }

    @Test
    @DisplayName("I02 - data de geração nula")
    void i02_dataGeracaoNula() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setDataGeracao(null);

        assertRegra("Data de geração inválida", ingresso);
    }

    @Test
    @DisplayName("I03 - código nulo")
    void i03_codigoNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setCodigo(null);

        assertRegra("Código inválido", ingresso);
    }

    @Test
    @DisplayName("I04 - código vazio")
    void i04_codigoVazio() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setCodigo("");

        assertRegra("Código inválido", ingresso);
    }

    @Test
    @DisplayName("I05 - status nulo")
    void i05_statusNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setStatus(null);

        assertRegra("Status inválido", ingresso);
    }

    @Test
    @DisplayName("I06 - status vazio")
    void i06_statusVazio() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setStatus("");

        assertRegra("Status inválido", ingresso);
    }

    @Test
    @DisplayName("I07 - lote nulo")
    void i07_loteNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setLote(null);

        assertRegra("Lote inválido", ingresso);
    }

    @Test
    @DisplayName("I08 - id do lote nulo")
    void i08_idDoLoteNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.getLote().setId(null);

        assertRegra("Lote inválido", ingresso);
    }

    @Test
    @DisplayName("I09 - cliente nulo")
    void i09_clienteNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setCliente(null);

        assertRegra("Cliente inválido", ingresso);
    }

    @Test
    @DisplayName("I10 - id do cliente nulo")
    void i10_idDoClienteNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.getCliente().setId(null);

        assertRegra("Cliente inválido", ingresso);
    }

    @Test
    @DisplayName("I11 - item da compra nulo")
    void i11_itemCompraNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.setItemCompra(null);

        assertRegra("Item da compra inválido", ingresso);
    }

    @Test
    @DisplayName("I12 - id do item da compra nulo")
    void i12_idDoItemCompraNulo() {
        Ingresso ingresso = criarIngressoBase();
        ingresso.getItemCompra().setId(null);

        assertRegra("Item da compra inválido", ingresso);
    }
}