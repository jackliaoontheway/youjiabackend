package com.polarj;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@EnableJpaRepositories(basePackages = { "com.**.repository" }, includeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = { EntityRepositoryScanFilter.class }) }, 
        entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "entityTransaction")
public class PolarjJpaConfig
{
    private final static String unitName = "NormalUnit";

    @Bean("entityProperties")
    @Qualifier("entityProperties")
    @ConfigurationProperties(prefix = "entity.datasource")
    public DataSourceProperties entityPropertiesBean()
    {
        DataSourceProperties ds = new DataSourceProperties();
        return ds;
    }

    @Bean("entityDataSource")
    @Qualifier("entityDataSource")
    @ConfigurationProperties("entity.datasource.hikari")
    public HikariDataSource entityDataSourceBean(@Qualifier("entityProperties") DataSourceProperties properties)
    {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean("entityManager")
    @Qualifier("entityManager")
    public EntityManager entityManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf)
    {
        return emf.createEntityManager();
    }

    @Bean("entityManagerFactory")
    @Qualifier("entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier("entityDataSource") DataSource ds,
            PolarjHibernateConfig hibernateCfg)
    {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(ds);
        HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
        hjva.setShowSql(hibernateCfg.getShowSql());
        hjva.setGenerateDdl(hibernateCfg.getGenerateDdl());
        lef.setJpaVendorAdapter(hjva);
        lef.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", hibernateCfg.getDdlAuto());
        lef.getJpaPropertyMap().put("hibernate.dialect", hibernateCfg.getDialect());
        lef.setPackagesToScan(hibernateCfg.getPackagesToScan());
        lef.setPersistenceUnitName(unitName);
        lef.afterPropertiesSet();
        return lef.getObject();
    }

    @Primary
    @Bean("entityTransaction")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf)
    {
        JpaTransactionManager jtm = new JpaTransactionManager(emf);
        jtm.setPersistenceUnitName(unitName);
        return jtm;
    }

    @Bean("entityOpenEntityInView")
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter()
    {
        OpenEntityManagerInViewFilter oemivf = new OpenEntityManagerInViewFilter();
        oemivf.setEntityManagerFactoryBeanName("entityManagerFactory");
        oemivf.setPersistenceUnitName(unitName);
        return oemivf;
    }
}
