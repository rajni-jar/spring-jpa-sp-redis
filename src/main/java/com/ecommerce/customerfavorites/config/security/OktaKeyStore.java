package com.ecommerce.customerfavorites.config.security;

import com.ecommerce.customerfavorites.common.GlobalConstants;
import com.ecommerce.customerfavorites.config.security.auth.OktaKey;
import com.ecommerce.customerfavorites.exception.BaseApiException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Slf4j
@Component
@ConfigurationProperties(GlobalConstants.OKTA_PROPERTY_PREFIX)
public class OktaKeyStore {

  @Autowired
  private RestTemplate restTemplate;

  private List<String> discoveryEndpoints;

  /**
   * This method will define okat keys bean.
   *
   * @return list of keys.
   */
  @Bean(name = GlobalConstants.OKTA_KEY_LIST)
  public List<OktaKey> getOktaPublicKeys() {
    log.info("OktaKeyStore >> getOktaPublicKeys >> Fetching Okta keys");
    if (discoveryEndpoints != null && !discoveryEndpoints.isEmpty()) {
      List<OktaKey> oktaKeys = new ArrayList<>();
      try {
        for (String discoveryEndpoint : discoveryEndpoints) {
          ResponseEntity<LinkedHashMap<String, List<OktaKey>>> oktaDiscoveryResponse =
              restTemplate.exchange(
                  discoveryEndpoint,
                  HttpMethod.GET,
                  HttpEntity.EMPTY,
                  new ParameterizedTypeReference<LinkedHashMap<String, List<OktaKey>>>() {});
          oktaKeys.addAll(oktaDiscoveryResponse.getBody().get(GlobalConstants.KEYS));
        }
        return oktaKeys;
      } catch (Exception ex) {
        log.error("Exception occurred while pulling the okta keys ", ex);
      }
    }
    throw new BaseApiException("Exception occurred while pulling the okta keys");
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
