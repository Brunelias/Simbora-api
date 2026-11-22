package com.simbora.simbora_api.config;

import com.simbora.simbora_api.model.entity.Admin;
import com.simbora.simbora_api.model.repository.AdminRepository;
import com.simbora.simbora_api.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInicial implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final AdminService adminService;

    @Value("${app.admin.nome:Administrador}")
    private String nome;

    @Value("${app.admin.email:}")
    private String email;

    @Value("${app.admin.celular:00000000000}")
    private String celular;

    @Value("${app.admin.senha:}")
    private String senha;

    @Override
    public void run(String... args) {

        if (adminRepository.count() > 0) {
            return;
        }

        if (email.isEmpty() || senha.isEmpty()) {
            log.warn("Nenhum admin cadastrado e app.admin.email/app.admin.senha não configurados");
            return;
        }

        Admin admin = new Admin();
        admin.setNome(nome);
        admin.setEmail(email);
        admin.setCelular(celular);
        admin.setSenha(senha);

        adminService.salvar(admin);
        log.info("Administrador inicial criado: {}", email);
    }
}