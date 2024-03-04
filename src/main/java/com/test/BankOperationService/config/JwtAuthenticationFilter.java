package com.test.BankOperationService.config;

import com.test.BankOperationService.model.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Фильтр аутентификации на основе JWT токенов.
 * Этот фильтр проверяет наличие и валидность JWT токена в заголовке запроса.
 * Если токен действителен, устанавливает аутентификацию для текущего пользователя.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  /**
   * Проверяет наличие и валидность JWT токена в заголовке запроса.
   * Если токен действителен, устанавливает аутентификацию для текущего пользователя.
   *
   * @param request     HTTP запрос
   * @param response    HTTP ответ
   * @param filterChain цепочка фильтров
   * @throws ServletException если происходит ошибка сервлета
   * @throws IOException      если происходит ошибка ввода-вывода
   */
  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    // Проверяем, является ли запрос аутентификационным запросом
    if (request.getServletPath().contains("/auth")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Извлекаем JWT токен из заголовка запроса
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      // Если токен отсутствует или некорректен, пропускаем запрос дальше
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);

    // Извлекаем имя пользователя из JWT токена
    userEmail = jwtService.extractUsername(jwt);

    // Проверяем, установлена ли аутентификация для текущего пользователя
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // Загружаем информацию о пользователе из базы данных
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      // Проверяем, является ли JWT токен действительным и не истек ли его срок действия
      var isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked())
              .orElse(false);
      // Если токен действителен, устанавливаем аутентификацию для пользователя
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Передаем запрос дальше по цепочке фильтров
    filterChain.doFilter(request, response);
  }
}