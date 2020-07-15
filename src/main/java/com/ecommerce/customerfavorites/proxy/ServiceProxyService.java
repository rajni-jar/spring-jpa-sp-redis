package com.ecommerce.customerfavorites.proxy;

import static java.util.Optional.ofNullable;
import static org.springframework.http.ResponseEntity.status;

import com.ecommerce.customerfavorites.common.GlobalConstants;
import com.ecommerce.customerfavorites.exception.BaseApiException;
import com.ecommerce.customerfavorites.exception.NoDataFoundException;
import com.ecommerce.customerfavorites.model.CatalogProductResponse;
import com.ecommerce.customerfavorites.model.UpcCatalogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServiceProxyService {

  @Autowired
  private CatalogClient catalogClient;


  /**
   * This method calls Catalog product Service and returns response.
   *
   * @return product detail as response
   */
  public CatalogProductResponse getCatalogUpcDesc(UpcCatalogRequest statusRequest) {
    CatalogProductResponse customerOrderStatusList = null;
    try {
      long startTime = System.currentTimeMillis();
      ResponseEntity<CatalogProductResponse> orderStatusResponse =
          ofNullable(catalogClient.getCatalogProducts(statusRequest))
              .orElseThrow(
                  () ->
                      new BaseApiException(
                          "Catalog product Service :: calling get product details "
                              + GlobalConstants.FAILED_DUE_TO_EMPTY_RESPONSE_DATA));

      long endTime = System.currentTimeMillis();
      log.info("Catalog product Service :: Completed call to get product details");
      validateServiceClientResponse(orderStatusResponse, "Catalog product Service");
      customerOrderStatusList = orderStatusResponse.getBody();
    } catch (Exception ex) {
      log.error(
          "Catalog product Service :: calling get product details failed for request#{} "
              + "with exception#{}",
          statusRequest,
          ex.getMessage());
    }
    return customerOrderStatusList;
  }

  private void validateServiceClientResponse(ResponseEntity response,
                                             String serviceName) throws BaseApiException {
    //TODO Need to cleanup this as exception is taken care against
    // FeignClientConfigurtion::CustomErrorDecoder
    ResponseEntity responseEntity =
        ofNullable(response).orElseThrow(() -> new BaseApiException("Response is null!"));
    ofNullable(responseEntity)
        .ifPresent(
            responseObject -> {
              if (responseObject.getStatusCode()
                  == status(HttpStatus.FORBIDDEN).build().getStatusCode()) {
                throw new BaseApiException("Response is forbidden!");
              }
              if (responseObject.getStatusCode()
                  == status(HttpStatus.NO_CONTENT).build().getStatusCode()
                  || responseObject.getBody() == null) {
                throw new NoDataFoundException();
              }
              if (responseObject.getStatusCode() != status(HttpStatus.OK).build().getStatusCode()) {
                throw new BaseApiException(
                    "Error calling "
                        + serviceName
                        + " :Error code = "
                        + responseObject.getStatusCode()
                        + " Message = "
                        + responseObject.getStatusCode().getReasonPhrase());
              }
            });
  }
}
