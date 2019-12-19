package com.example.transactiondynamic.datasource1;

import com.example.transactiondynamic.datasource.SeverntDB;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 17:29 2019/12/9
 */
@Configuration
@EnableConfigurationProperties(SeverntDB.class)
@MapperScan(basePackages = "com.example.transactiondynamic.mapper.severnt", sqlSessionFactoryRef = "severntSqlSessionFactory")
public class SeverntDataSourceConfig {

    /*********************************************************************************
     * 第二个数据库的配置
     * @param severntDB
     * @return
     * @throws SQLException
     */
    @Bean("severntDataSource")
    public DataSource severntDataSource(SeverntDB severntDB)throws SQLException {
        MysqlXADataSource dataSource = new MysqlXADataSource();
        dataSource.setUrl(severntDB.getUrl());
        dataSource.setUser(severntDB.getUsername());
        dataSource.setPassword(severntDB.getPassword());
        dataSource.setPinGlobalTxToPhysicalConnection(true);
        return dataSource;
    }

    @Bean("severntSqlSessionFactory")
    public SqlSessionFactory severntSqlSessionFactory(@Qualifier("severntDataSource") DataSource dataSource)throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/severnt/*.xml"));
        return sessionFactoryBean.getObject();
    }

    @Bean("severntTransaction")
    public DataSourceTransactionManager masterTransaction(@Qualifier("severntDataSource") DataSource dataSource){
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }

}
