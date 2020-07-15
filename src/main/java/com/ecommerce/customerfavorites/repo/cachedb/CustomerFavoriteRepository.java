package com.ecommerce.customerfavorites.repo.cachedb;

import com.ecommerce.customerfavorites.model.domain.cached.CustomerFavoriteProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/* @author Rajni Kanth Tupakula */
@Repository
public interface CustomerFavoriteRepository
    extends JpaRepository<CustomerFavoriteProduct, Integer> {

  @Query(value = "EXECUTE GetCustomerFavorites :CustomerId", nativeQuery = true)
  List<CustomerFavoriteProduct> getCustomerByCustomerId(@Param("CustomerId") int customerId);
}
