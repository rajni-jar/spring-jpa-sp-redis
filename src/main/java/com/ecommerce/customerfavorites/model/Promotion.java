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
public class Promotion implements Serializable {
  private String promoDescription;
  private String promoEndDate;
  private String promoType;
  private String promoText;
  private Integer triggerQuantity;
}
