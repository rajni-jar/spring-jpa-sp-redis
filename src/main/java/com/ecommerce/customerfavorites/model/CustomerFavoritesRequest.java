package com.ecommerce.customerfavorites.model;

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
public class CustomerFavoritesRequest {
  private int storeId;
  private String email;
  private int pageNumber;
  private int fetchSize;
}
