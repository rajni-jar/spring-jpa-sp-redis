package com.ecommerce.customerfavorites.config.security.auth;

import static java.util.Optional.ofNullable;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/* @author Rajni Kanth Tupakula */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
  private static final String BEARER = "Bearer";

  public TokenAuthenticationFilter(final RequestMatcher requiresAuth) {
    super(requiresAuth);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    final String param =
        ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse(request.getParameter("t"));

    final String token =
        ofNullable(param)
            .map(value -> StringUtils.removeStart(value, BEARER))
            .map(String::trim)
            .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));

    final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
    return getAuthenticationManager().authenticate(auth);
  }

  @Override
  protected void successfulAuthentication(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain chain,
      final Authentication authResult)
      throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }
}
