package com.ecommerce.customerfavorites.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/** @author Rajni Kanth Tupakula */
@Configuration
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .paths(PathSelectors.ant("/api/**"))
        .apis(
            RequestHandlerSelectors.basePackage(
                "com.ecommerce.customerfavorites.controller"))
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "Customer Favorites API", // title
        "Customer Favourites API.", // description
        "Version 1.0", // version
        "", // terms of service URL
        new Contact("Rajni Kanth Tupakula", "www.example.com", "rajanikanth@test.com"),
        "License of API",
        "API license URL",
        Collections.emptyList()); // contact info
  }
}
