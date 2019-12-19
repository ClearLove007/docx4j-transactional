//package com.example.transactiondynamic.datasource;
//
//import com.mysql.cj.jdbc.MysqlXADataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//
///**
// * @Author: XueWeiDong
// * @Description:
// * @Date: 16:55 2019/12/9
// */
//@Configuration
//@EnableConfigurationProperties(MasterDB.class)
//@MapperScan(basePackages = "com.example.transactiondynamic.mapper.master", sqlSessionFactoryRef = "masterSqlSessionFactory")
//public class MasterDataSourcesConfig {
//
//    /*********************************************************************************
//     * 第一个数据库的配置
//     * @param masterDB
//     * @return
//     * @throws SQLException
//     */
//    @Bean("masterDataSource")
//    public DataSource masterDataSource(MasterDB masterDB)throws SQLException {
//        MysqlXADataSource dataSource = new MysqlXADataSource();
//        dataSource.setUrl(masterDB.getUrl());
//        dataSource.setUser(masterDB.getUsername());
//        dataSource.setPassword(masterDB.getPassword());
//        dataSource.setPinGlobalTxToPhysicalConnection(true);
//
//        //交给事务管理器
//        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
//        atomikosDataSourceBean.setXaDataSource(dataSource);
//        atomikosDataSourceBean.setUniqueResourceName("master");
//        return atomikosDataSourceBean;
//    }
//
//    @Bean("masterSqlSessionFactory")
//    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource)throws Exception {
//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/master/*.xml"));
//
//
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.setMapUnderscoreToCamelCase(true);
//
//
//        sessionFactoryBean.setConfiguration(configuration);
//        return sessionFactoryBean.getObject();
//    }
//
//}
