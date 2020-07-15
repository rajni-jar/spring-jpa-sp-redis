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
import org.springframework.context.annotation.Primary;
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
    basePackages = "com.ecommerce.customerfavorites.repo.cachedb",
    entityManagerFactoryRef = "cachedbEntityManagerFactory",
    transactionManagerRef = "cachedbTransactionManager")
public class CacheDbDataSourceConfiguration {

  @Value("${app.datasource.cachedb.driverClassName}")
  private String driverClassName;
  @Value("${app.datasource.cachedb.url}")
  private String url;
  @Value("${app.datasource.cachedb.username}")
  private String userName;
  @Value("${app.datasource.cachedb.password:}")
  private String password;

  /**
   * This method will create cachedbDataSource with provided config.
   *
   * @return producer factory.
   */
  @Primary
  @Bean(name = "cachedbDataSource")
  public DataSource cachedbDataSource() {
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
   * This method will create cachedbEntityManagerFactory with provided config.
   *
   * @return producer factory.
   */
  @Primary
  @Bean(name = "cachedbEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean cachedbEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(cachedbDataSource())
        .packages(CustomerFavoriteProduct.class)
        .persistenceUnit("cachedbEntityManager")
        .build();
  }

  /**
   * This method will create cachedbTransactionManager with provided config.
   *
   * @return producer factory.
   */
  @Primary
  @Bean(name = "cachedbTransactionManager")
  public PlatformTransactionManager cachedbTransactionManager(
      final @Qualifier("cachedbEntityManagerFactory") LocalContainerEntityManagerFactoryBean
              cachedbEntityManagerFactory) {
    return new JpaTransactionManager(cachedbEntityManagerFactory.getObject());
  }
}
