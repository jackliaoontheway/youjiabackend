package com.polarj;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@ConditionalOnProperty("workflow.enable")
@EnableJpaRepositories(basePackages = {
        "com.polarj.workflow.model.repository" }, entityManagerFactoryRef = "wfManagerFactory", transactionManagerRef = "wfTransaction")
public class WorkflowJpaConfig
{
    private final static String unitName = "WorkflowUnit";

    @Bean("wfProperties")
    @Qualifier("wfProperties")
    @ConfigurationProperties(prefix = "workflow.datasource")
    public DataSourceProperties entityPropertiesBean()
    {
        DataSourceProperties ds = new DataSourceProperties();
        return ds;
    }

    @Bean("wfDataSource")
    @Qualifier("wfDataSource")
    @ConfigurationProperties("workflow.datasource.hikari")
    public HikariDataSource entityDataSourceBean(@Qualifier("wfProperties") DataSourceProperties properties)
    {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("wfEntityManager")
    @Qualifier("wfEntityManager")
    public EntityManager entityManager(@Qualifier("wfManagerFactory") EntityManagerFactory emf)
    {
        EntityManager em = emf.createEntityManager();
        return em;
    }

    @Bean("wfManagerFactory")
    @Qualifier("wfManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier("wfDataSource") DataSource ds,
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
        lef.setPackagesToScan("com.polarj.workflow.model");
        lef.setPersistenceUnitName(unitName);
        lef.afterPropertiesSet();
        return lef.getObject();
    }

    @Bean("wfTransaction")
    public PlatformTransactionManager transactionManager(@Qualifier("wfManagerFactory") EntityManagerFactory emf)
    {
        JpaTransactionManager jtm = new JpaTransactionManager(emf);
        jtm.setPersistenceUnitName(unitName);
        return jtm;
    }

    @Bean("wfOpenEntityInView")
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter()
    {
        OpenEntityManagerInViewFilter oemivf = new OpenEntityManagerInViewFilter();
        oemivf.setEntityManagerFactoryBeanName("wfManagerFactory");
        oemivf.setPersistenceUnitName(unitName);
        return oemivf;
    }
}