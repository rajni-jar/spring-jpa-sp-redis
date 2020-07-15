package com.ecommerce.customerfavorites.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/*
 * @author Rajni Kanth Tupakula
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInfo implements Serializable {

  @JsonProperty("id")
  private String id;

  @JsonProperty("price")
  private Double price;

  @JsonProperty("basePrice")
  private Double basePrice;

  @JsonProperty("promoEndDate")
  private String promoEndDate;

  @JsonProperty("pricePer")
  private Double pricePer;

  @JsonProperty("sellByWeight")
  private String sellByWeight;

  @JsonProperty("averageWeight")
  private String averageWeight;

  @JsonProperty("maxWeight")
  private String maxWeight;

  @JsonProperty("upc")
  private String upc;

  @JsonProperty("edited")
  private Boolean edited;

  @JsonProperty("versionid")
  private Integer versionid;

  @JsonProperty("lineadded")
  private String lineadded;

  @JsonProperty("aisleId")
  private String aisleId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("image")
  private String image;

  @JsonProperty("estimatedDiscount")
  private Double estimatedDiscount;

  @JsonProperty("departmentName")
  private String departmentName;

  @JsonProperty("aisleName")
  private String aisleName;

  @JsonProperty("shelfName")
  private String shelfName;

  @JsonProperty("promoDescription")
  private String promoDescription;

  @JsonProperty("promoType")
  private String promoType;

  @JsonProperty("promoText")
  private String promoText;

  @JsonProperty("triggerQuantity")
  private Double triggerQuantity;

  @JsonProperty("comment")
  private String comment;

  @JsonProperty("substitutionValue")
  private String substitutionValue;

  @JsonProperty("inlinePromoId")
  private String inlinePromoId;

  @JsonProperty("inlinePromoImage")
  private String inlinePromoImage;

  @JsonProperty("quantity")
  private Double quantity;

  @JsonProperty("details")
  private String details;

  @JsonProperty("nutritionFacts")
  private String nutritionFacts;

  @JsonProperty("displayType")
  private Integer displayType;

  @JsonProperty("unitOfMeasure")
  private String unitOfMeasure;

  @JsonProperty("restrictedValue")
  private Integer restrictedValue;

  @JsonProperty("salesRank")
  private Integer salesRank;

  @JsonProperty("width")
  private Double width;

  @JsonProperty("depth")
  private Double depth;

  @JsonProperty("height")
  private Double height;

  @JsonProperty("zonecode")
  private String zonecode;

  @JsonProperty("prop65WarningTypeCD")
  private String prop65WarningTypeCD;

  @JsonProperty("prop65WarningText")
  private String prop65WarningText;

  @JsonProperty("prop65WarningIconRequired")
  private Boolean prop65WarningIconRequired;

  @JsonProperty("productDetailsUrl")
  private String productDetailsUrl;

  @JsonProperty("similarItemsUrl")
  private String similarItemsUrl;
}
