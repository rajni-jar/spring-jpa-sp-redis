package com.ecommerce.customerfavorites.config.security.auth;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
/* @author Rajni Kanth Tupakula */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final RequestMatcher PUBLIC_URLS =
      new OrRequestMatcher(
          new AntPathRequestMatcher("/api/v1/**"),
          new AntPathRequestMatcher("/v2/api-docs"),
          new AntPathRequestMatcher("/swagger-resources/**"),
          new AntPathRequestMatcher("/swagger-ui.html**"),
          new AntPathRequestMatcher("/webjars/**"),
          new AntPathRequestMatcher("/webjars/**"),
          new AntPathRequestMatcher("/csrf"),
          new AntPathRequestMatcher("/actuator/health"),
          new AntPathRequestMatcher("favicon.ico"));

  private static final RequestMatcher PROTECTED_URLS =
      new OrRequestMatcher(new AntPathRequestMatcher("/api/v1/customer/**"));

  private TokenAuthenticationProvider provider;

  SecurityConfig(final TokenAuthenticationProvider provider) {
    super();
    this.provider = requireNonNull(provider);
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(provider);
  }

  //    @Override
  //    public void configure(final WebSecurity web) {
  //        web.ignoring().requestMatchers(PUBLIC_URLS);
  //    }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable().sessionManagement()
        .sessionCreationPolicy(STATELESS)
        .and()
        .exceptionHandling()
        // this entry point handles when you request a protected page and you are not yet
        // authenticated
        .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
        .and()
        .authenticationProvider(provider)
        .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/api/v1/customer/**")
        .authenticated()
        .anyRequest()
        .permitAll();
  }

  @Bean
  TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
    final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(successHandler());
    return filter;
  }

  @Bean
  SimpleUrlAuthenticationSuccessHandler successHandler() {
    final SimpleUrlAuthenticationSuccessHandler successHandler =
        new SimpleUrlAuthenticationSuccessHandler();
    successHandler.setRedirectStrategy((request, response, url) -> {});
    return successHandler;
  }

  /** Disable Spring boot automatic filter registration. */
  @Bean
  FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
    final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
    registration.setEnabled(false);
    return registration;
  }

  @Bean
  AuthenticationEntryPoint forbiddenEntryPoint() {
    return new HttpStatusEntryPoint(FORBIDDEN);
  }
}