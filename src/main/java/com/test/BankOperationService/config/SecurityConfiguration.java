package com.test.BankOperationService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Конфигурация безопасности приложения.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/auth/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "Terms**"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    /**
     * Конфигурирует цепочку фильтров безопасности.
     *
     * @param http конфигуратор HTTP безопасности
     * @return цепочка фильтров безопасности
     * @throws Exception если происходит ошибка конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем защиту от CSRF-атак
                .csrf(AbstractHttpConfigurer::disable)
                // Настраиваем правила авторизации запросов
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL) // Устанавливает правила авторизации для указанных URL
                                .permitAll() // Разрешает доступ без аутентификации
                                .anyRequest() // Устанавливает правила для всех остальных запросов
                                .authenticated() // Требует аутентификацию для всех остальных запросов
                )
                // Устанавливаем управление сессиями без сохранения состояния
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                // Устанавливаем провайдер аутентификации
                .authenticationProvider(authenticationProvider)
                // Добавляем JWT фильтр аутентификации перед стандартным фильтром Spring Security
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Настраиваем выход из системы
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        // Возвращаем построенную цепочку фильтров безопасности
        return http.build();
    }
}
