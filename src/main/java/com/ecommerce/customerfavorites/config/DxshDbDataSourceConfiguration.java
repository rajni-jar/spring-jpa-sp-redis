package com.ecommerce.customerfavorites.config;

import com.ecommerce.customerfavorites.model.domain.dxsh.Customer;
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

/* @author Rajni Kanth Tupakula */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.ecommerce.customerfavorites.repo.dxsh",
    entityManagerFactoryRef = "dxshEntityManagerFactory",
    transactionManagerRef = "dxshTransactionManager")
public class DxshDbDataSourceConfiguration {

  @Value("${app.datasource.dxsh.driverClassName}")
  private String driverClassName;
  @Value("${app.datasource.dxsh.url}")
  private String url;
  @Value("${app.datasource.dxsh.username}")
  private String userName;
  @Value("${app.datasource.dxsh.password:}")
  private String password;

  /**
   * This method will create dxshDataSource with provided config.
   *
   * @return producer factory.
   */
  @Bean
  public DataSource dxshDataSource() {
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
  @Bean(name = "dxshEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean dxshEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(dxshDataSource())
        .packages(Customer.class)
        .persistenceUnit("dxshEntityManager")
        .build();
  }

  /**
   * This method will create dxshTransactionManager with provided config.
   *
   * @return producer factory.
   */
  @Bean(name = "dxshTransactionManager")
  public PlatformTransactionManager dxshTransactionManager(
      final @Qualifier("dxshEntityManagerFactory") LocalContainerEntityManagerFactoryBean
              dxshEntityManagerFactory) {
    return new JpaTransactionManager(dxshEntityManagerFactory.getObject());
  }
}
