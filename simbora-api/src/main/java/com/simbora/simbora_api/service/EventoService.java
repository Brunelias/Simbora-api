package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Evento;
import com.simbora.simbora_api.model.repository.EventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventoService {

    private EventoRepository repository;

    public EventoService(EventoRepository repository) {
        this.repository = repository;
    }

    public List<Evento> getEventos() {
        return repository.findAll();
    }

    public Optional<Evento> getEventoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Evento salvar(Evento evento) {
        validar(evento);
        return repository.save(evento);
    }

    @Transactional
    public void excluir(Evento evento) {
        Objects.requireNonNull(evento.getId());
        repository.delete(evento);
    }

    public void validar(Evento evento) {

        if (evento.getNome() == null || evento.getNome().trim().equals("")) {
            throw new RegraNegocioException("Título inválido");
        }

        if (evento.getDescricao() == null || evento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descrição inválida");
        }

        if (evento.getDataInicio() == null) {
            throw new RegraNegocioException("Data de início inválida");
        }

        if (evento.getDataFim() == null) {
            throw new RegraNegocioException("Data de fim inválida");
        }

        if (evento.getLocal() == null || evento.getLocal().trim().equals("")) {
            throw new RegraNegocioException("Local inválido");
        }

        if (evento.getCapacidadeMaxima() == null || evento.getCapacidadeMaxima() <= 0) {
            throw new RegraNegocioException("Capacidade máxima inválida");
        }

        if (evento.getStatus() == null || evento.getStatus().trim().equals("")) {
            throw new RegraNegocioException("Status inválido");
        }

        if (evento.getOrganizador() == null || evento.getOrganizador().getId() == null) {
            throw new RegraNegocioException("Organizador inválido");
        }
    }
}