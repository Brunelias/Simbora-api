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
import org.springframework.security.core.Authentication;

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

    @GetMapping("/minhas")
    @ApiOperation("Obter compras do cliente autenticado")
    public ResponseEntity getMinhasCompras(Authentication authentication) {

        String email = authentication.getName();

        List<Compra> compras = service.getComprasByClienteEmail(email);

        return ResponseEntity.ok(
                compras.stream()
                        .map(CompraDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping
    @ApiOperation("Obter todas as compras")
    public ResponseEntity get(Authentication authentication) {

        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Compra> compras = isAdmin
                ? service.getCompras()
                : service.getComprasByOrganizadorEmail(email);

        return ResponseEntity.ok(
                compras.stream()
                        .map(CompraDTO::create)
                        .collect(Collectors.toList())
        );
    }

    public Compra converter(CompraDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        Compra compra = modelMapper.map(dto, Compra.class);

        Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());

        if (!cliente.isPresent()) {
            throw new RegraNegocioException("Cliente não encontrado");
        }

        compra.setCliente(cliente.get());

        if (dto.getIdFormaPagamento() != null) {

            Optional<FormaPagamento> formaPagamento =
                    formaPagamentoService.getFormaPagamentoById(dto.getIdFormaPagamento());

            if (!formaPagamento.isPresent()) {
                throw new RegraNegocioException("Forma de pagamento não encontrada");
            }

            compra.setFormaPagamento(formaPagamento.get());
        }

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