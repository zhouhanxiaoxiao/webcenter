package com.cibr.logincenter.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.cibr.logincenter.dao.logincenter", sqlSessionFactoryRef = "LoginCenterSqlSessionFactory")
public class LoginCenterDataSourceConfig {

    @Bean(name = "LoginCenterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.login")
    public DataSource getLoginCenterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "LoginCenterSqlSessionFactory")
    @Primary
    public SqlSessionFactory loginCenterSqlSessionFactory(@Qualifier("LoginCenterDataSource") DataSource datasource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:generator/logincenter/*.xml"));
        return bean.getObject();
    }

    @Bean("LoginCenterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate loginCenterSqlSessionTemplate(@Qualifier("LoginCenterSqlSessionFactory") SqlSessionFactory sessionfactory){
        return new SqlSessionTemplate(sessionfactory);
    }

}
