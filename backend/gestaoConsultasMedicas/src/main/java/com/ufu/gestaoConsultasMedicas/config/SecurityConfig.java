package com.ufu.gestaoConsultasMedicas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Desabilitar proteção CSRF
                .authorizeHttpRequests()
                .anyRequest().permitAll()  // Permitir todas as requisições sem autenticação ou permissões específicas
                .and()
                .headers().frameOptions().disable()  // Desabilita as proteções contra iframes
                .and()
                .httpBasic().disable();  // Desativa a autenticação básica
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Encoder para criptografia de senhas
    }
}


