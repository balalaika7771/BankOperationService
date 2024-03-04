package com.test.BankOperationService.config;

import com.test.BankOperationService.model.user.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Конфигурация приложения, определяющая настройки аутентификации и безопасности.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final PersonRepository repository;

    /**
     * Создает сервис пользователей для аутентификации.
     *
     * @return сервис пользователей
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByAnyEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    /**
     * Создает провайдер аутентификации.
     *
     * @return провайдер аутентификации
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Создает менеджер аутентификации.
     *
     * @param config конфигурация аутентификации
     * @return менеджер аутентификации
     * @throws Exception если не удалось получить менеджер аутентификации из конфигурации
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Создает кодировщик паролей.
     *
     * @return кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
