package com.simbora.simbora_api.config;

import com.simbora.simbora_api.security.JwtAuthFilter;
import com.simbora.simbora_api.security.JwtService;
import com.simbora.simbora_api.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, autenticacaoService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(autenticacaoService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()

// --- Público (sem login) ---
                .antMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                .antMatchers(HttpMethod.GET,  "/api/v1/eventos/**").permitAll()
                .antMatchers(HttpMethod.GET,  "/api/v1/lotes/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/clientes").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/organizadores").permitAll()

// --- Rotas específicas (antes dos /** genéricos) ---
                .antMatchers(HttpMethod.PUT, "/api/v1/clientes/me")
                .hasAnyRole("CLIENTE", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/compras/minhas")
                .hasAnyRole("ADMIN", "ORGANIZADOR", "CLIENTE")
                .antMatchers(HttpMethod.PUT, "/api/v1/organizadores/me")
                .hasAnyRole("ORGANIZADOR", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/compras")
                .hasAnyRole("ADMIN", "CLIENTE")

// --- Somente Admin ---
                .antMatchers("/api/v1/admin/**")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,   "/api/v1/formas-pagamento/**")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,    "/api/v1/formas-pagamento/**")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/formas-pagamento/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/clientes/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/organizadores/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/ingressos/**")
                .hasRole("ADMIN")

// --- Admin e Organizador ---
                .antMatchers("/api/v1/eventos/**")
                .hasAnyRole("ADMIN", "ORGANIZADOR")
                .antMatchers("/api/v1/lotes/**")
                .hasAnyRole("ADMIN", "ORGANIZADOR")
                .antMatchers(HttpMethod.GET, "/api/v1/compras/**")
                .hasAnyRole("ADMIN", "ORGANIZADOR")
                .antMatchers("/api/v1/checkin/**")
                .hasAnyRole("ADMIN", "ORGANIZADOR")

// --- Admin, Organizador e Cliente ---
                .antMatchers(HttpMethod.GET, "/api/v1/formas-pagamento/**")
                .hasAnyRole("ADMIN", "ORGANIZADOR", "CLIENTE")

// --- Autenticação (qualquer papel) ---
                .antMatchers("/api/v1/auth/**").authenticated()

                .anyRequest().authenticated()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }
}