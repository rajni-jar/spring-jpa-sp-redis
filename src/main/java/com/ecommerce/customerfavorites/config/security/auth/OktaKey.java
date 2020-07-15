package com.ecommerce.customerfavorites.config.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OktaKey {

  private String kty;
  private String alg;
  private String kid;
  private String use;
  private String e;
  private String n;
}
