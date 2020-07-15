package com.ecommerce.customerfavorites.model;

import java.util.Collection;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@ToString
public class User extends org.springframework.security.core.userdetails.User {

  private final String email;
  private final String jwtToken;
  private final String firstName;
  private final String lastName;

  /**
   * Construct user object.
   *
   * @param username input name.
   * @param email input mail.
   * @param jwtToken input token.
   * @param password input pwd.
   * @param firstName input firstname.
   * @param lastName input lastname.
   * @param authorities input authorities.
   */
  public User(
      String username,
      String email,
      String jwtToken,
      String password,
      String firstName,
      String lastName,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.email = email;
    this.jwtToken = jwtToken;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public String getJwtToken() {
    return this.jwtToken;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
}
