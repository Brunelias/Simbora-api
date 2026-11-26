package com.simbora.simbora_api.ServiceTests;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.service.OrganizadorService;
import com.simbora.simbora_api.model.entity.Organizador;
import com.simbora.simbora_api.model.repository.OrganizadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OrganizadorServiceTest {

    @Mock
    private OrganizadorRepository organizadorRepository;

    private OrganizadorService service;

    @BeforeEach
    void setUp() {
        service = new OrganizadorService(organizadorRepository);
    }

    // ---------- métodos auxiliares ----------

    /**
     * Organizador-base (caso nominal, com valores mínimos válidos): nome,
     * e-mail, celular, documento e senha com 1 caractere ("A").
     */
    private Organizador criarOrganizadorBase() {
        Organizador organizador = new Organizador();
        organizador.setNome("A");
        organizador.setEmail("A");
        organizador.setCelular("A");
        organizador.setDocumento("A");
        organizador.setSenha("A");
        return organizador;
    }

    private void assertRegra(String mensagemEsperada, Organizador organizador) {
        RegraNegocioException ex = assertThrows(
                RegraNegocioException.class,
                () -> service.validar(organizador));
        assertEquals(mensagemEsperada, ex.getMessage());
    }

    // ---------- casos de teste ----------

    @Test
    @DisplayName("O01 - organizador-base válido (valores mínimos)")
    void o01_organizadorBaseValido() {
        Organizador organizador = criarOrganizadorBase();

        assertDoesNotThrow(() -> service.validar(organizador));
        verifyNoInteractions(organizadorRepository);
    }

    @Test
    @DisplayName("O02 - nome nulo")
    void o02_nomeNulo() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setNome(null);

        assertRegra("Nome inválido", organizador);
    }

    @Test
    @DisplayName("O03 - nome vazio")
    void o03_nomeVazio() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setNome("");

        assertRegra("Nome inválido", organizador);
    }

    @Test
    @DisplayName("O04 - e-mail nulo")
    void o04_emailNulo() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setEmail(null);

        assertRegra("Email inválido", organizador);
    }

    @Test
    @DisplayName("O05 - e-mail vazio")
    void o05_emailVazio() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setEmail("");

        assertRegra("Email inválido", organizador);
    }

    @Test
    @DisplayName("O06 - celular nulo")
    void o06_celularNulo() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setCelular(null);

        assertRegra("Celular inválido", organizador);
    }

    @Test
    @DisplayName("O07 - celular vazio")
    void o07_celularVazio() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setCelular("");

        assertRegra("Celular inválido", organizador);
    }

    @Test
    @DisplayName("O08 - documento nulo")
    void o08_documentoNulo() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setDocumento(null);

        assertRegra("Documento inválido", organizador);
    }

    @Test
    @DisplayName("O09 - documento vazio")
    void o09_documentoVazio() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setDocumento("");

        assertRegra("Documento inválido", organizador);
    }

    @Test
    @DisplayName("O10 - senha nula")
    void o10_senhaNula() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setSenha(null);

        assertRegra("Senha inválida", organizador);
    }

    @Test
    @DisplayName("O11 - senha vazia")
    void o11_senhaVazia() {
        Organizador organizador = criarOrganizadorBase();
        organizador.setSenha("");

        assertRegra("Senha inválida", organizador);
    }
}