package com.ecommerce.customerfavorites.service;

import static java.util.Optional.ofNullable;

import com.ecommerce.customerfavorites.exception.NoDataFoundException;
import com.ecommerce.customerfavorites.model.CatalogProductResponse;
import com.ecommerce.customerfavorites.model.CustomerFavoritesRequest;
import com.ecommerce.customerfavorites.model.CustomerFavouritesResponse;
import com.ecommerce.customerfavorites.model.ProductInfo;
import com.ecommerce.customerfavorites.model.ProductResponse;
import com.ecommerce.customerfavorites.model.UpcCatalogRequest;
import com.ecommerce.customerfavorites.model.domain.cached.CustomerFavoriteProduct;
import com.ecommerce.customerfavorites.model.domain.dxsh.Customer;
import com.ecommerce.customerfavorites.proxy.ServiceProxyService;
import com.ecommerce.customerfavorites.repo.cachedb.CustomerFavoriteRepository;
import com.ecommerce.customerfavorites.repo.dxsh.CustomerRepository;
import com.ecommerce.customerfavorites.repo.gwdb.GwdbCustomerFavoriteRepository;
import com.sun.istack.Nullable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/*
 * @author Rajni Kanth Tupakula
 */
@Service
@Slf4j
public class CustomerFavouritesService {

  @Autowired private CustomerFavoriteRepository customerFavoriteRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private GwdbCustomerFavoriteRepository gwdbCustomerFavoriteRepository;
  @Autowired private ServiceProxyService serviceProxyService;
  @Autowired private CacheManager cacheManager;

  protected boolean isRequestValid(CustomerFavoritesRequest customerFavoritesRequest) {
    if (customerFavoritesRequest == null) {
      return false;
    }
    return true;
  }

  public List<CustomerFavoriteProduct> doProcessing(
      CustomerFavoritesRequest customerFavoritesRequest) {
    Customer customer = getCustomer(customerFavoritesRequest.getEmail());
    return getCustomerFavoriteProducts(
        customer.getCustomerId(),
        customerFavoritesRequest.getStoreId(),
        customerFavoritesRequest.getPageNumber(),
        customerFavoritesRequest.getFetchSize());
  }

  private List<CustomerFavoriteProduct> getCustomerFavoriteProducts(
      int customerId, int store, int pageNumber, int fetchSize) {
    log.info("Getting customer favourite products with given customerId#{}", customerId);
    Cache cache = cacheManager.getCache("customerProductCache");
    return
        getCustomerFavoriteProducts(customerId);
  }

  private List<CustomerFavoriteProduct> getCustomerFavoriteProducts(int customerId) {
    List<CustomerFavoriteProduct> customerFavoriteProducts = null;
    try {
      customerFavoriteProducts = customerFavoriteRepository.getCustomerByCustomerId(customerId);
    } catch (Exception ex) {
      log.error(
          "Customer favourite products are not found in 'CacheDB' table and it throws#{} so looking"
              + " into 'GWDB' table with given customerId#{}",
          ex.getMessage(),
          customerId);
    }

    if (CollectionUtils.isEmpty(customerFavoriteProducts)) {
      customerFavoriteProducts =
          Optional.ofNullable(gwdbCustomerFavoriteRepository.getCustomerByCustomerId(customerId))
              .orElseThrow(
                  () -> {
                    log.error(
                        "Customer favourite products are not found in 'GWDB' "
                            + "table for customerId#{}",
                        customerId);
                    return new NoDataFoundException(
                        "Customer favourite products are not found in 'GWDB' "
                            + "table for customerId#{}"
                            + customerId);
                  });
    }
    return customerFavoriteProducts;
  }

  private void addToCache(Cache cache, CatalogProductResponse catalogProductResponse) {
    catalogProductResponse
        .getProducts()
        .forEach(
            productResponse -> {
              cache.put(productResponse.getId(), productResponse);
            });
  }

  @NotNull
  private List<String> getProductIdsNotFoundInCache(
      int store, Cache cache, List<String> productIds, List<ProductResponse> productResponses) {
    log.info("Getting product info from cache for store#{}", store);
    return productIds.stream()
        .filter(
            s -> {
              Cache.ValueWrapper cachedObject = cache.get(s + "_" + store);
              if (Optional.ofNullable(cachedObject).isPresent()) {
                productResponses.add((ProductResponse) cachedObject.get());
                return false;
              }
              return true;
            })
        .collect(Collectors.toList());
  }

  @NotNull
  private List<String> getProductIds(List<CustomerFavoriteProduct> customerFavoriteProducts) {
    return customerFavoriteProducts
        .parallelStream()
        .map(customerFavoriteProduct -> String.valueOf(customerFavoriteProduct.getProductId()))
        .collect(Collectors.toList());
  }

  private CustomerFavouritesResponse getCustomerFavouritesResponse(
      CatalogProductResponse catalogProductResponse,
      List<CustomerFavoriteProduct> customerFavoriteProducts,
      int pageNumber,
      int totalPages) {
    Map<Integer, List<CustomerFavoriteProduct>> customerFavoriteProductGroupByProductId =
        getCustomerProductByProductId(customerFavoriteProducts);
    List<ProductInfo> productInfos =
        getProductInfos(catalogProductResponse, customerFavoriteProductGroupByProductId);
    return CustomerFavouritesResponse.builder()
        .products(productInfos)
        .totalPages(totalPages)
        .currentPage(pageNumber)
        .totalProducts(customerFavoriteProducts.size())
        .build();
  }

  @NotNull
  private List<ProductInfo> getProductInfos(
      CatalogProductResponse catalogProductResponse,
      Map<Integer, List<CustomerFavoriteProduct>> customerFavoriteProductGroupByProductId) {
    return catalogProductResponse
        .getProducts()
        .parallelStream()
        .map(
            productResponse -> {
              CustomerFavoriteProduct customerFavoriteProduct =
                  getSubstitutionCode(
                      customerFavoriteProductGroupByProductId, productResponse.getBpnId());
              return buildProductInfo(productResponse, customerFavoriteProduct);
            })
        .collect(Collectors.toList());
  }

  private Map<Integer, List<CustomerFavoriteProduct>> getCustomerProductByProductId(
      List<CustomerFavoriteProduct> customerFavoriteProducts) {
    return customerFavoriteProducts
        .parallelStream()
        .collect(
            Collectors.groupingBy(
                customerFavoriteProduct -> customerFavoriteProduct.getProductId()));
  }

  @Nullable
  private CustomerFavoriteProduct getSubstitutionCode(
      Map<Integer, List<CustomerFavoriteProduct>> customerFavoriteProductGroupByProductId,
      String bpnId) {
    List<CustomerFavoriteProduct> favouriteProduct =
        customerFavoriteProductGroupByProductId.get(bpnId);
    if (!CollectionUtils.isEmpty(favouriteProduct)) {
      return favouriteProduct.parallelStream().findFirst().orElse(null);
    }
    return null;
  }

  @NotNull
  private ProductInfo buildProductInfo(
      ProductResponse productResponse, CustomerFavoriteProduct customerFavoriteProduct) {
    ProductInfo productInfo =
        ProductInfo.builder()
            .unitOfMeasure(productResponse.getUnitOfMeasure())
            .id(productResponse.getBpnId())
            .estimatedDiscount(0D)
            .versionid(0)
            .lineadded(LocalDateTime.now(Clock.systemUTC()).toString())
            .upc(productResponse.getPrimaryUpc())
            .name(productResponse.getName())
            .image(productResponse.getImageUrl())
            .restrictedValue(productResponse.getRestrictedValue())
            .quantity(Double.valueOf(1D))
            .salesRank(productResponse.getSalesRank())
            .zonecode(productResponse.getZonecode())
            .build();
    Optional.ofNullable(customerFavoriteProduct)
        .ifPresent(
            customerFavoriteProd -> {
              productInfo.setComment(customerFavoriteProd.getComments());
              productInfo.setSubstitutionValue(customerFavoriteProd.getSubstitutionCode());
            });
    buildAisle(productResponse, productInfo);
    buildDimension(productResponse, productInfo);
    buildPrinceInfo(productResponse, productInfo);
    buildPro65(productResponse, productInfo);
    buildPromo(productResponse, productInfo);
    productInfo.setDisplayType(
        Optional.ofNullable(productResponse.getDisplayType())
            .map(s -> Integer.valueOf(s))
            .orElse(0));
    productInfo.setDepartmentName(
        Optional.ofNullable(productResponse.getDepartment())
            .map(department -> department.getName())
            .orElse(""));
    productInfo.setShelfName(
        Optional.ofNullable(productResponse.getShelf())
            .map(department -> department.getName())
            .orElse(""));
    return productInfo;
  }

  private void buildAisle(ProductResponse productResponse, ProductInfo productInfo) {
    Optional.ofNullable(productResponse.getAisle())
        .ifPresent(
            aisle -> {
              productInfo.setAisleId(aisle.getId());
              productInfo.setAisleName(aisle.getName());
            });
  }

  private void buildDimension(ProductResponse productResponse, ProductInfo productInfo) {
    Optional.ofNullable(productResponse.getDimension())
        .ifPresent(
            dimension -> {
              productInfo.setWidth(dimension.getWidth());
              productInfo.setHeight(dimension.getHeight());
              productInfo.setDepth(dimension.getDepth());
            });
  }

  private void buildPro65(ProductResponse productResponse, ProductInfo productInfo) {
    Optional.ofNullable(productResponse.getPro65())
        .ifPresent(
            pro65 -> {
              productInfo.setProp65WarningIconRequired(pro65.isProp65WarningIconRequired());
              productInfo.setProp65WarningText(pro65.getProp65WarningText());
              productInfo.setProp65WarningTypeCD(pro65.getProp65WarningTypeCD());
            });
  }

  private void buildPromo(ProductResponse productResponse, ProductInfo productInfo) {
    Optional.ofNullable(productResponse.getPromotion())
        .ifPresent(
            promotion -> {
              productInfo.setPromoEndDate(promotion.getPromoEndDate());
              productInfo.setPromoDescription(promotion.getPromoDescription());
              productInfo.setPromoText(promotion.getPromoText());
              productInfo.setPromoType(promotion.getPromoType());
              productInfo.setTriggerQuantity(
                  ofNullable(promotion.getTriggerQuantity())
                      .map(s -> Double.valueOf(s))
                      .orElse(0D));
            });
  }

  private void buildPrinceInfo(ProductResponse productResponse, ProductInfo productInfo) {
    Optional.ofNullable(productResponse.getPriceInfo())
        .ifPresent(
            priceInfo -> {
              productInfo.setPrice(
                  ofNullable(priceInfo.getPrice()).map(s -> Double.valueOf(s)).orElse(0D));
              productInfo.setBasePrice(
                  ofNullable(priceInfo.getBasePrice()).map(s -> Double.valueOf(s)).orElse(0D));
              productInfo.setPricePer(
                  ofNullable(priceInfo.getPricePer()).map(s -> Double.valueOf(s)).orElse(0D));
              productInfo.setSellByWeight(priceInfo.getSellByWeight());
              productInfo.setMaxWeight(priceInfo.getMaxWeight());
              productInfo.setAverageWeight(priceInfo.getAverageWeight());
            });
  }

  private CatalogProductResponse getCatalogProductResponse(
      List<String> partitionProductIds, int storeId) {
    log.info("Calling catalog service storeId#{} with productIds#{}", storeId, partitionProductIds);
    if (!CollectionUtils.isEmpty(partitionProductIds)) {
      UpcCatalogRequest upcCatalogRequest =
          UpcCatalogRequest.builder()
              .ids(partitionProductIds)
              .includeInactive(true)
              .requestType("BPN")
              .storeId(String.valueOf(storeId))
              .build();
      return serviceProxyService.getCatalogUpcDesc(upcCatalogRequest);
    }
    return CatalogProductResponse.builder().products(new ArrayList<>()).build();
  }

  private Customer getCustomer(String email) {
    log.info("Getting customer details with given email#{}", email);
    return ofNullable(customerRepository.getCustomerByEmail(email))
        .orElseThrow(
            () -> {
              log.error("Customer details are not found for emailId#{}", email);
              return new NoDataFoundException(
                  "Customer details are not found for emailId#" + email);
            });
  }
}
