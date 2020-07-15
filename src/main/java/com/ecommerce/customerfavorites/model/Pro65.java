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
public class Pro65 implements Serializable {
  private String prop65WarningTypeCD;
  private String prop65WarningText;
  private boolean prop65WarningIconRequired;

}
