package com.ecommerce.customerfavorites.repo.dxsh;

import com.ecommerce.customerfavorites.model.domain.dxsh.Customer;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/* @author Rajni Kanth Tupakula */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

  @Query(value = "EXECUTE GetCustomerId :EmailAddress", nativeQuery = true)
  Customer getCustomerByEmail(@Param("EmailAddress") String email);

  @Query(
      value =
          "call CreateCustomer1 :FirstName,:LastName,:Initials,:Title,:ClubCard,"
              + ":DefaultSubRule,:AcceptPromotions,:OtherMailing,:FavoriteRequest,"
              + ":EmailAddress,:CertSerialNumber,:HomeShoppingCD,:Creator,:DataProtection,"
              + ":UserProfile,:PermissionMarketing,:StoreId,:ServiceId,:Address1,:Address2,"
              + ":City,:State,:ZipCode,:PhoneDay,:PhoneEvening,:PhoneMobile,:GridReference,"
              + ":Version,:MaxCSL,:CurrentDate,:AccountStatus,:HowRecruited,:CCerOptedIn,"
              + ":LoyaltyStatus,:CustomerId,:ZipPlus4,:ReferrerId,:ExternalId,:TermsVersionId,"
              + ":CustomerTypeId,:B2bBusinessName,:B2eBusinessCode,:GID,:BannerName",
      nativeQuery = true)
  // @Procedure(procedureName = "CreateCustomer1")
  void createCustomer(
      @Param("FirstName") String firstName,
      @Param("LastName") String lastName,
      @Param("Initials") String initials,
      @Param("Title") String title,
      @Param("ClubCard") String clubCard,
      @Param("DefaultSubRule") String defaultSubRule,
      @Param("AcceptPromotions") String acceptPromotions,
      @Param("OtherMailing") String otherMailing,
      @Param("FavoriteRequest") String favoriteRequest,
      @Param("EmailAddress") String emailAddress,
      @Param("CertSerialNumber") String certSerialNumber,
      @Param("HomeShoppingCD") String homeShoppingCD,
      @Param("Creator") String creator,
      @Param("DataProtection") String dataProtection,
      @Param("UserProfile") String userProfile,
      @Param("PermissionMarketing") String permissionMarketing,
      @Param("StoreId") Integer storeId,
      @Param("ServiceId") Short serviceId,
      @Param("Address1") String address1,
      @Param("Address2") String address2,
      @Param("City") String city,
      @Param("State") String state,
      @Param("ZipCode") String zipCode,
      @Param("PhoneDay") String phoneDay,
      @Param("PhoneEvening") String phoneEvening,
      @Param("PhoneMobile") String phoneMobile,
      @Param("GridReference") String gridReference,
      @Param("Version") String version,
      @Param("MaxCSL") Short maxCsl,
      @Param("CurrentDate") Timestamp currentDate,
      @Param("AccountStatus") int accountStatus,
      @Param("HowRecruited") Short howRecruited,
      @Param("CCerOptedIn") String ccerOptedIn,
      @Param("LoyaltyStatus") Short loyaltyStatus,
      @Param("CustomerId") int customerId,
      @Param("ZipPlus4") String zipPlus4,
      @Param("ReferrerId") int referrerId,
      @Param("ExternalId") String externalId,
      @Param("TermsVersionId") int termsVersionId,
      @Param("CustomerTypeId") int customerTypeId,
      @Param("B2bBusinessName") String b2bBusinessName,
      @Param("B2eBusinessCode") String b2eBusinessCode,
      @Param("GID") String gid,
      @Param("BannerName") String bannerName);
}
