package com.ecommerce.customerfavorites.model.domain.dxsh;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/* @author Rajni Kanth Tupakula */

@Data
@Entity
public class Customer {

  @Id
  @Column(name = "CustomerId")
  private int customerId;

  @Column(name = "StoreId")
  private int storeId;
}
