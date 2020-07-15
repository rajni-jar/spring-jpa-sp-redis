package com.ecommerce.customerfavorites.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Rajni Kanth Tupakula
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CustomerFavouritesResponse {
  private int totalProducts;
  private int currentPage;
  private int totalPages;
  private boolean ack;
  private List<ProductInfo> products;
}
