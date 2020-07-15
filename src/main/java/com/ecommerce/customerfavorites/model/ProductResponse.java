package com.ecommerce.customerfavorites.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProductResponse implements Serializable {

  private String id;
  private String status;
  private String storeId;
  private String bpnId;
  private String primaryUpc;
  private String offerType;
  private String name;
  private String imageUrl;
  private String displayType;
  private String unitOfMeasure;
  private Integer restrictedValue;
  private Integer salesRank;
  private String zonecode;
  private String unitQuantity;
  private String enrichmentDesc;
  private Integer maxPurchaseQty;
  private Department department;
  private Shelf shelf;
  private Aisle aisle;
  private Promotion promotion;
  private PriceInfo priceInfo;
  private Organization organization;
  private Dimension dimension;
  private Pro65 pro65;
  private boolean snapEligible;

}
