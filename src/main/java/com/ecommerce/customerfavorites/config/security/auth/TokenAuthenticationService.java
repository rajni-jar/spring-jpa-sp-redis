package com.ecommerce.customerfavorites.config.security.auth;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

import com.ecommerce.customerfavorites.common.GlobalConstants;
import com.ecommerce.customerfavorites.config.security.JwtClaimsParser;
import com.ecommerce.customerfavorites.enums.Role;
import com.ecommerce.customerfavorites.model.User;
import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/* @author Rajni Kanth Tupakula */

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class TokenAuthenticationService implements UserAuthenticationService {

  @Autowired
  private JwtClaimsParser jwtClaimsParser;

  /**
   * This method will validate the jwtToken.
   *
   * @param jwtToken input token to validate.
   * @return valid user.
   */
  @Override
  public Optional<User> findByToken(final String jwtToken) {

    JWTClaimsSet claims = jwtClaimsParser.validateToken(jwtToken);
    // If claims is not null, it means the safeway token is valid. Return an Authentication token
    // with 'ROLE_USER' authority
    if (claims != null) {
      log.info(
          "User token is valid {} with gid {}", claims.toString(), claims.getClaim(GlobalConstants.GID));
      List<SimpleGrantedAuthority> authorities =
          Arrays.asList(new SimpleGrantedAuthority(Role.ROLE_USER.name()));

      // Users are identified by their 'gid' (or 'guid'). This will be present in the Okta token
      // with claim 'gid'
      String firstName = (claims.getClaim("dnm") != null) ? claims.getClaim("dnm").toString() : "";
      String lastName = (claims.getClaim("lnm") != null) ? claims.getClaim("lnm").toString() : "";
      User user =
          new User(
              claims.getClaim(GlobalConstants.GID).toString(),
              claims.getSubject(),
              GlobalConstants.BEARER_HEADER + jwtToken,
              "",
              firstName,
              lastName,
              authorities);
      return Optional.ofNullable(user);
    }
    throw new BadCredentialsException("Bad Authentication Token");
  }

  @Override
  public void logout(User user) {}
}
