package com.ecommerce.customerfavorites.controller;

import com.ecommerce.customerfavorites.common.GlobalConstants;
import com.ecommerce.customerfavorites.model.CustomerFavoritesRequest;
import com.ecommerce.customerfavorites.model.CustomerFavouritesResponse;
import com.ecommerce.customerfavorites.model.User;
import com.ecommerce.customerfavorites.service.CustomerFavouritesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/* @author Rajni Kanth Tupakula */
@RestController
@RequestMapping(
    value = GlobalConstants.OSCO_SERVICE_API_PREFIX + GlobalConstants.CUSTOMER,
    produces = MediaType.APPLICATION_JSON_VALUE)
@Api(
    value = GlobalConstants.OSCO_SERVICE_API_PREFIX + GlobalConstants.CUSTOMER,
    description = GlobalConstants.CUSTOMER_FAVOURITE_DESCRIPTION,
    tags = GlobalConstants.CUSTOMER_FAVOURITE_DESCRIPTION)
@Slf4j
public class CustomerFavouritesController {

  @Autowired private CustomerFavouritesService customerFavouritesService;

  /**
   * This service will customer's favourite products.
   *
   * @param storeId input storeId
   * @param user input OKTA user
   * @return list of favorite products
   */
  @GetMapping("/producthistory")
  @ApiOperation(
      value = "This service will fetches list of customer favourite products",
      notes = "PurchasedItemRequest",
      response = CustomerFavouritesResponse.class,
      responseContainer = "ResponseEntity",
      authorizations = {@Authorization(value = GlobalConstants.BEARER_TOKEN_STRING)})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = GlobalConstants.SUCCESS,
            message = "List of customer purchased items",
            response = CustomerFavouritesResponse.class),
        @ApiResponse(
            code = GlobalConstants.INFO_NOT_FOUND,
            message = "Customer purchased items information not found for search criteria"),
        @ApiResponse(code = GlobalConstants.UNAUTHORIZED_ACCESS, message = "Not Authenticated"),
        @ApiResponse(code = GlobalConstants.NOT_AUTHORIZED, message = "Not Authorized"),
        @ApiResponse(
            code = GlobalConstants.INTERNAL_SERVER_ERROR,
            message = "Internal Server Error"),
        @ApiResponse(code = GlobalConstants.INVALID_INPUT_VALUE, message = "Invalid Input Value")
      })
  public ResponseEntity getCustomerByEmail(
      @RequestParam("storeId") int storeId,
      @RequestParam(name = "fetchSize", defaultValue = "10", required = false)
          @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "fetch size should be integer")
          int fetchSize,
      @RequestParam(name = "pageNumber", defaultValue = "0", required = false)
          @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "page number should be integer")
          int pageNumber,
      @ApiIgnore @AuthenticationPrincipal final User user) {
    CustomerFavoritesRequest customerFavoritesRequest =
        CustomerFavoritesRequest.builder()
            .storeId(storeId)
            .email(user.getEmail())
            .pageNumber(pageNumber)
            .fetchSize(fetchSize)
            .build();
    return ResponseEntity.ok(
        customerFavouritesService.doProcessing(customerFavoritesRequest));
  }
}
