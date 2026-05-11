package com.simbora.simbora_api.service;

import com.simbora.simbora_api.exception.RegraNegocioException;
import com.simbora.simbora_api.model.entity.Admin;
import com.simbora.simbora_api.model.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {

    private AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    public List<Admin> getAdmins() {
        return repository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Admin salvar(Admin admin) {
        validar(admin);
        return repository.save(admin);
    }

    @Transactional
    public void excluir(Admin admin) {
        Objects.requireNonNull(admin.getId());
        repository.delete(admin);
    }

    public void validar(Admin admin) {

        if (admin.getNome() == null || admin.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (admin.getEmail() == null || admin.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }

        if (admin.getCelular() == null || admin.getCelular().trim().equals("")) {
            throw new RegraNegocioException("Celular inválido");
        }
    }
}