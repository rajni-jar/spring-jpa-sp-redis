package com.ecommerce.customerfavorites.model.domain.gwdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/*
 * @author Rajni Kanth Tupakula
 */
@Data
@Entity
public class GwdbCustomerFavoriteProduct {
  @Id
  @Column(name = "productId")
  private int productId;
  @Column(name = "IsFavorite")
  private int isFavorite;
  @Column(name = "Comments")
  private String comments;
  @Column(name = "SubstitutionNote")
  private String substitutionNote;
  @Column(name = "SubstitutionCode")
  private String substitutionCode;
  @Column(name = "PreviousOrderQuantity")
  private int previousOrderQuantity;

}
