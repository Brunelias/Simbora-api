package com.simbora.simbora_api.api.controller;

import com.simbora.simbora_api.api.dto.CompraDTO;
import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Cliente;
import com.simbora.simbora_api.model.entity.Compra;
import com.simbora.simbora_api.model.entity.FormaPagamento;
import com.simbora.simbora_api.model.entity.ItemCompra;
import com.simbora.simbora_api.model.entity.Lote;
import com.simbora.simbora_api.service.ClienteService;
import com.simbora.simbora_api.service.CompraService;
import com.simbora.simbora_api.service.FormaPagamentoService;
import com.simbora.simbora_api.service.LoteService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/compras")
@RequiredArgsConstructor
@Api("API de Compras")
@CrossOrigin
public class CompraController {

    private final CompraService service;
    private final ClienteService clienteService;
    private final FormaPagamentoService formaPagamentoService;
    private final LoteService loteService;

    @GetMapping
    public ResponseEntity get() {
        List<Compra> compras = service.getCompras();

        return ResponseEntity.ok(
                compras.stream()
                        .map(CompraDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma compra")
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id da compra") Long id) {

        Optional<Compra> compra = service.getCompraById(id);

        if (!compra.isPresent()) {
            return new ResponseEntity("Compra não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(CompraDTO.create(compra.get()));
    }

    @PostMapping
    @ApiOperation("Salvar uma nova compra")
    public ResponseEntity post(@RequestBody CompraDTO dto) {

        try {
            Compra compra = converter(dto);

            compra = service.salvar(compra);

            return new ResponseEntity(CompraDTO.create(compra), HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma compra")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody CompraDTO dto) {

        if (!service.getCompraById(id).isPresent()) {
            return new ResponseEntity("Compra não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            Compra compra = converter(dto);

            compra.setId(id);

            compra = service.salvar(compra);

            return ResponseEntity.ok(CompraDTO.create(compra));

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma compra")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Compra> compra = service.getCompraById(id);

        if (!compra.isPresent()) {
            return new ResponseEntity("Compra não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(compra.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Compra converter(CompraDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        Compra compra = modelMapper.map(dto, Compra.class);

        Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());

        if (!cliente.isPresent()) {
            throw new RegraNegocioException("Cliente não encontrado");
        }

        Optional<FormaPagamento> formaPagamento =
                formaPagamentoService.getFormaPagamentoById(dto.getIdFormaPagamento());

        if (!formaPagamento.isPresent()) {
            throw new RegraNegocioException("Forma de pagamento não encontrada");
        }

        compra.setCliente(cliente.get());
        compra.setFormaPagamento(formaPagamento.get());

        if (dto.getItens() != null) {
            List<ItemCompra> itens = dto.getItens()
                    .stream()
                    .map(itemDTO -> {

                        ItemCompra item = modelMapper.map(itemDTO, ItemCompra.class);

                        Optional<Lote> lote = loteService.getLoteById(itemDTO.getIdLote());

                        if (!lote.isPresent()) {
                            throw new RegraNegocioException("Lote não encontrado");
                        }

                        item.setLote(lote.get());

                        return item;
                    })
                    .collect(Collectors.toList());

            compra.setItens(itens);
        }

        return compra;
    }
}