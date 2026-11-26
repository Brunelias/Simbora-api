package com.simbora.simbora_api.ServiceTests;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.service.CompraService;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.model.entity.Compra;
import com.simbora.simbora_api.model.entity.FormaPagamento;
import com.simbora.simbora_api.model.entity.ItemCompra;
import com.simbora.simbora_api.model.entity.Lote;
import com.simbora.simbora_api.model.repository.CompraRepository;
import com.simbora.simbora_api.model.repository.IngressoRepository;
import com.simbora.simbora_api.model.repository.LoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private IngressoRepository ingressoRepository;

    @Mock
    private LoteRepository loteRepository;

    private CompraService service;

    @BeforeEach
    void setUp() {
        service = new CompraService(compraRepository, ingressoRepository, loteRepository);
    }

    // ---------- métodos auxiliares ----------

    private Lote criarLote(Long id, Integer quantidade, Boolean gratuito) {
        Lote lote = new Lote();
        lote.setId(id);
        lote.setQuantidade(quantidade);
        lote.setGratuito(gratuito);
        return lote;
    }

    private ItemCompra criarItem(Integer quantidade, Lote lote) {
        ItemCompra item = new ItemCompra();
        item.setQuantidade(quantidade);
        item.setLote(lote);
        return item;
    }

    /**
     * Compra-base (caso nominal): cliente id 1, forma de pagamento informada,
     * 1 item com quantidade 1 e lote pago {id 1, quantidade 1, gratuito = false}.
     */
    private Compra criarCompraBase() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        FormaPagamento forma = new FormaPagamento();
        forma.setId(1L);

        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setFormaPagamento(forma);
        compra.setItens(new ArrayList<>(Arrays.asList(
                criarItem(1, criarLote(1L, 1, false)))));
        return compra;
    }

    private ItemCompra primeiroItem(Compra compra) {
        return compra.getItens().get(0);
    }

    private void assertRegra(String mensagemEsperada, Compra compra) {
        RegraNegocioException ex = assertThrows(
                RegraNegocioException.class,
                () -> service.validar(compra));
        assertEquals(mensagemEsperada, ex.getMessage());
    }

    // ---------- casos de teste ----------

    @Test
    @DisplayName("C01 - compra-base válida (valores mínimos)")
    void c01_compraBaseValida() {
        Compra compra = criarCompraBase();

        assertDoesNotThrow(() -> service.validar(compra));
        verifyNoInteractions(ingressoRepository);
    }

    @Test
    @DisplayName("C02 - cliente nulo")
    void c02_clienteNulo() {
        Compra compra = criarCompraBase();
        compra.setCliente(null);

        assertRegra("Cliente inválido", compra);
    }

    @Test
    @DisplayName("C03 - id do cliente nulo")
    void c03_idDoClienteNulo() {
        Compra compra = criarCompraBase();
        compra.getCliente().setId(null);

        assertRegra("Cliente inválido", compra);
    }

    @Test
    @DisplayName("C04 - lista de itens nula")
    void c04_itensNulo() {
        Compra compra = criarCompraBase();
        compra.setItens(null);

        assertRegra("A compra precisa ter pelo menos um item", compra);
    }

    @Test
    @DisplayName("C05 - lista de itens vazia")
    void c05_itensVazio() {
        Compra compra = criarCompraBase();
        compra.setItens(new ArrayList<>());

        assertRegra("A compra precisa ter pelo menos um item", compra);
    }

    @Test
    @DisplayName("C06 - quantidade do item nula")
    void c06_quantidadeDoItemNula() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).setQuantidade(null);

        assertRegra("Quantidade inválida", compra);
    }

    @Test
    @DisplayName("C07 - quantidade do item igual a 0 (limite inferior - 1)")
    void c07_quantidadeDoItemZero() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).setQuantidade(0);

        assertRegra("Quantidade inválida", compra);
    }

    @Test
    @DisplayName("C08 - lote do item nulo")
    void c08_loteNulo() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).setLote(null);

        assertRegra("Lote inválido", compra);
    }

    @Test
    @DisplayName("C09 - id do lote nulo")
    void c09_idDoLoteNulo() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).getLote().setId(null);

        assertRegra("Lote inválido", compra);
    }

    @Test
    @DisplayName("C10 - quantidade disponível do lote nula")
    void c10_quantidadeDoLoteNula() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).getLote().setQuantidade(null);

        assertRegra("Quantidade indisponível no lote", compra);
    }

    @Test
    @DisplayName("C11 - quantidade do lote = 0, menor que a do item (limite - 1)")
    void c11_quantidadeDoLoteInsuficiente() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).getLote().setQuantidade(0);

        assertRegra("Quantidade indisponível no lote", compra);
    }

    @Test
    @DisplayName("C12 - campo gratuito do lote nulo")
    void c12_gratuitoNulo() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).getLote().setGratuito(null);

        assertRegra("Tipo do lote inválido", compra);
    }

    @Test
    @DisplayName("C13 - lote gratuito com 2 ingressos (limite + 1)")
    void c13_loteGratuitoComDoisIngressos() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).setQuantidade(2);
        primeiroItem(compra).getLote().setQuantidade(2);
        primeiroItem(compra).getLote().setGratuito(true);

        assertRegra("Só é permitido um ingresso por pessoa em lotes gratuitos", compra);
    }

    @Test
    @DisplayName("C14 - lote gratuito e cliente já possui ingresso (mock = true)")
    void c14_clienteJaPossuiIngressoGratuito() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).getLote().setGratuito(true);
        when(ingressoRepository.existsByClienteIdAndLoteId(1L, 1L)).thenReturn(true);

        assertRegra("Cliente já possui ingresso deste lote gratuito", compra);
        verify(ingressoRepository).existsByClienteIdAndLoteId(1L, 1L);
    }

    @Test
    @DisplayName("C15 - lote gratuito sem ingresso prévio e sem forma de pagamento (válido)")
    void c15_loteGratuitoSemFormaDePagamento() {
        Compra compra = criarCompraBase();
        compra.setFormaPagamento(null);
        primeiroItem(compra).getLote().setGratuito(true);
        when(ingressoRepository.existsByClienteIdAndLoteId(1L, 1L)).thenReturn(false);

        assertDoesNotThrow(() -> service.validar(compra));
        verify(ingressoRepository).existsByClienteIdAndLoteId(1L, 1L);
    }

    @Test
    @DisplayName("C16 - lote pago sem forma de pagamento")
    void c16_lotePagoSemFormaDePagamento() {
        Compra compra = criarCompraBase();
        compra.setFormaPagamento(null);

        assertRegra("Forma de pagamento obrigatória para compra paga", compra);
    }

    @Test
    @DisplayName("C17 - dois itens pagos válidos (laço com 2 iterações)")
    void c17_doisItensPagos() {
        Compra compra = criarCompraBase();
        compra.getItens().add(criarItem(1, criarLote(2L, 1, false)));

        assertDoesNotThrow(() -> service.validar(compra));
        verifyNoInteractions(ingressoRepository);
    }

    @Test
    @DisplayName("C18 - um item gratuito e um pago, com forma de pagamento (válido)")
    void c18_itemGratuitoEItemPago() {
        Compra compra = criarCompraBase();
        primeiroItem(compra).getLote().setGratuito(true);
        compra.getItens().add(criarItem(1, criarLote(2L, 1, false)));
        when(ingressoRepository.existsByClienteIdAndLoteId(1L, 1L)).thenReturn(false);

        assertDoesNotThrow(() -> service.validar(compra));
        verify(ingressoRepository).existsByClienteIdAndLoteId(1L, 1L);
    }
}