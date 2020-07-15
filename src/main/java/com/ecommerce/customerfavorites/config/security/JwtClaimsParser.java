package com.ecommerce.customerfavorites.config.security;

import com.ecommerce.customerfavorites.common.GlobalConstants;
import com.ecommerce.customerfavorites.config.security.auth.OktaKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtClaimsParser {

  private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

  /**
   * Construct JwtClaimsParser with jwtProcessor.
   *
   * @param oktaKeysList input oktaKeysList.
   * @param objectMapper input oktaKeysList.
   * @throws JsonProcessingException return error in case of json parser exception.
   * @throws ParseException return error in case of json parser exception.
   */
  @Autowired
  public JwtClaimsParser(List<OktaKey> oktaKeysList, ObjectMapper objectMapper)
      throws JsonProcessingException, ParseException {
    HashMap<String, List<OktaKey>> oktaKeysMap = new HashMap<>();
    oktaKeysMap.put(GlobalConstants.KEYS, oktaKeysList);
    jwtProcessor = new DefaultJWTProcessor<>();
    jwtProcessor.setJWSKeySelector(
        new JWSVerificationKeySelector<>(
            JWSAlgorithm.RS256,
            new ImmutableJWKSet<>(JWKSet.parse(objectMapper.writeValueAsString(oktaKeysMap)))));
  }

  /**
   * This method validates the given bearer token.
   *
   * @param token input token
   * @return JWTClaimsSet return set of claims
   */
  public JWTClaimsSet validateToken(String token) {
    try {
      return jwtProcessor.process(token, null);
    } catch (Exception e) {
      log.info("Encountered an error validating the token " + e.getMessage());
    }
    return null;
  }
}
