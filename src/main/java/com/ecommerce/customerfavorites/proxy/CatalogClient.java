package com.ecommerce.customerfavorites.proxy;

import com.ecommerce.customerfavorites.common.GlobalConstants;
import com.ecommerce.customerfavorites.model.CatalogProductResponse;
import com.ecommerce.customerfavorites.model.UpcCatalogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "CatalogClient",
    url = "${feign.catalog.url}",
    path = "${feign.catalog.contextPath}")
public interface CatalogClient {

  @PostMapping(
      consumes = GlobalConstants.CONTENT_TYPE,
      produces = GlobalConstants.CONTENT_TYPE,
      value = "/filter/products")
  ResponseEntity<CatalogProductResponse> getCatalogProducts(
      @RequestBody UpcCatalogRequest upcCatalogRequest);
}
