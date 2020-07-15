package com.ecommerce.customerfavorites.config.security.auth;

import com.ecommerce.customerfavorites.model.User;
import java.util.Optional;

/* @author Rajni Kanth Tupakula */
public interface UserAuthenticationService {

  /**
   * Finds a user by its dao-key.
   *
   * @param token user dao key
   * @return
   */
  Optional<User> findByToken(String token);

  /**
   * Logs out the given input {@code user}.
   *
   * @param user the user to logout
   */
  void logout(User user);
}
