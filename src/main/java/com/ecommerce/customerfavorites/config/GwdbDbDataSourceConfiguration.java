package com.ecommerce.customerfavorites.config;

import com.ecommerce.customerfavorites.model.domain.cached.CustomerFavoriteProduct;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * @author Rajni Kanth Tupakula
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.ecommerce.customerfavorites.repo.gwdb",
    entityManagerFactoryRef = "cachedbEntityManagerFactory",
    transactionManagerRef = "cachedbTransactionManager")
public class GwdbDbDataSourceConfiguration {

  @Value("${app.datasource.gwdb.driverClassName}")
  private String driverClassName;
  @Value("${app.datasource.gwdb.url}")
  private String url;
  @Value("${app.datasource.gwdb.username}")
  private String userName;
  @Value("${app.datasource.gwdb.password:}")
  private String password;

  /**
   * This method will create dxshDataSource with provided config.
   *
   * @return producer factory.
   */
  @Bean
  public DataSource gwdbDataSource() {
    HikariConfig config = new HikariConfig();
    config.setMaximumPoolSize(10);
    config.setDriverClassName(driverClassName);
    config.setJdbcUrl(url);
    config.setUsername(userName);
    if (StringUtils.isNotBlank(password)) {
      config.setPassword(password);
    }
    return new HikariDataSource(config);
  }

  /**
   * This method will create pdxshEntityManagerFactory with provided config.
   *
   * @return producer factory.
   */
  @Bean(name = "gwdbEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean gwdbEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(gwdbDataSource())
        .packages(CustomerFavoriteProduct.class)
        .persistenceUnit("gwdbEntityManager")
        .build();
  }

  /**
   * This method will create dxshTransactionManager with provided config.
   *
   * @return producer factory.
   */
  @Bean(name = "gwdbTransactionManager")
  public PlatformTransactionManager gwdbTransactionManager(
      final @Qualifier("gwdbEntityManagerFactory") LocalContainerEntityManagerFactoryBean
          gwdbEntityManagerFactory) {
    return new JpaTransactionManager(gwdbEntityManagerFactory.getObject());
  }

}
