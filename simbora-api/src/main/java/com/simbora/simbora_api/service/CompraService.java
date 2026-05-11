package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Compra;
import com.simbora.simbora_api.model.entity.Ingresso;
import com.simbora.simbora_api.model.entity.ItemCompra;
import com.simbora.simbora_api.model.repository.CompraRepository;
import com.simbora.simbora_api.model.repository.IngressoRepository;
import com.simbora.simbora_api.model.repository.LoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompraService {

    private CompraRepository repository;
    private IngressoRepository ingressoRepository;
    private LoteRepository loteRepository;

    public CompraService(
            CompraRepository repository,
            IngressoRepository ingressoRepository,
            LoteRepository loteRepository
    ) {
        this.repository = repository;
        this.ingressoRepository = ingressoRepository;
        this.loteRepository = loteRepository;
    }

    public List<Compra> getCompras() {
        return repository.findAll();
    }

    public Optional<Compra> getCompraById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Compra salvar(Compra compra) {
        validar(compra);

        Double valorTotal = 0.0;

        for (ItemCompra item : compra.getItens()) {

            item.setCompra(compra);

            item.setPrecoUnitario(item.getLote().getPrecoUnitario());
            item.setValorTotal(item.getQuantidade() * item.getPrecoUnitario());

            item.getLote().setQuantidade(
                    item.getLote().getQuantidade() - item.getQuantidade()
            );

            loteRepository.save(item.getLote());

            valorTotal += item.getValorTotal();
        }

        compra.setDataCompra(LocalDateTime.now());
        compra.setValorTotal(valorTotal);

        Compra compraSalva = repository.save(compra);

        gerarIngressos(compraSalva);

        return compraSalva;
    }

    @Transactional
    public void excluir(Compra compra) {
        Objects.requireNonNull(compra.getId());
        repository.delete(compra);
    }

    public void validar(Compra compra) {

        if (compra.getCliente() == null || compra.getCliente().getId() == null) {
            throw new RegraNegocioException("Cliente inválido");
        }

        if (compra.getFormaPagamento() == null || compra.getFormaPagamento().getId() == null) {
            throw new RegraNegocioException("Forma de pagamento inválida");
        }

        if (compra.getItens() == null || compra.getItens().isEmpty()) {
            throw new RegraNegocioException("A compra precisa ter pelo menos um item");
        }

        for (ItemCompra item : compra.getItens()) {

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new RegraNegocioException("Quantidade inválida");
            }

            if (item.getLote() == null || item.getLote().getId() == null) {
                throw new RegraNegocioException("Lote inválido");
            }

            if (item.getLote().getQuantidade() == null || item.getLote().getQuantidade() < item.getQuantidade()) {
                throw new RegraNegocioException("Quantidade indisponível no lote");
            }
        }
    }

    private void gerarIngressos(Compra compra) {

        for (ItemCompra item : compra.getItens()) {

            for (int i = 0; i < item.getQuantidade(); i++) {

                Ingresso ingresso = new Ingresso();

                ingresso.setDataGeracao(LocalDateTime.now());
                ingresso.setCodigo(UUID.randomUUID().toString());
                ingresso.setStatus("VALIDO");

                ingresso.setCliente(compra.getCliente());
                ingresso.setLote(item.getLote());
                ingresso.setItemCompra(item);

                ingressoRepository.save(ingresso);
            }
        }
    }
}