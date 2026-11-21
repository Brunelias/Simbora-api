package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.ItemCompra;
import com.simbora.simbora_api.model.repository.ItemCompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemCompraService {

    private ItemCompraRepository repository;

    public ItemCompraService(ItemCompraRepository repository) {
        this.repository = repository;
    }

    public List<ItemCompra> getItensCompra() {
        return repository.findAll();
    }

    public Optional<ItemCompra> getItemCompraById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ItemCompra salvar(ItemCompra itemCompra) {
        validar(itemCompra);

        itemCompra.setValorTotal(itemCompra.getQuantidade() * itemCompra.getValorUnitario());

        return repository.save(itemCompra);
    }

    @Transactional
    public void excluir(ItemCompra itemCompra) {
        Objects.requireNonNull(itemCompra.getId());
        repository.delete(itemCompra);
    }

    public void validar(ItemCompra itemCompra) {

        if (itemCompra.getQuantidade() == null || itemCompra.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade inválida");
        }

        if (itemCompra.getValorUnitario() == null || itemCompra.getValorUnitario() <= 0) {
            throw new RegraNegocioException("Valor unitário inválido");
        }

        if (itemCompra.getCompra() == null || itemCompra.getCompra().getId() == null) {
            throw new RegraNegocioException("Compra inválida");
        }

        if (itemCompra.getLote() == null || itemCompra.getLote().getId() == null) {
            throw new RegraNegocioException("Lote inválido");
        }
    }
}