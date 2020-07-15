package com.ecommerce.customerfavorites.config.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtUserAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 4486051028430050803L;

  private final Object principal;
  private Object credentials;

  /**
   * Construct JWT user Token.
   *
   * @param principal input principle user.
   * @param credentials input credentials.
   */
  public JwtUserAuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  /**
   * Construct JWT user Token.
   *
   * @param principal input principle user.
   * @param credentials input credentials.
   * @param authorities input authorities
   */
  public JwtUserAuthenticationToken(
      Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(true);
  }

  public Object getCredentials() {
    return this.credentials;
  }

  public Object getPrincipal() {
    return this.principal;
  }

  /**
   * Checking the user is authenticated.
   *
   * @param isAuthenticated input flag.
   */
  @Override
  public void setAuthenticated(boolean isAuthenticated) {
    if (isAuthenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - "
              + "use constructor which takes a GrantedAuthority list instead");
    }

    super.setAuthenticated(false);
  }

  /**
   * This method will erase credentails.
   */
  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    credentials = null;
  }
}
