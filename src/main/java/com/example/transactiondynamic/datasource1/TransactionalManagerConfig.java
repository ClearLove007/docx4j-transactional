package com.example.transactiondynamic.datasource1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 15:08 2019/12/10
 */
@Configuration
public class TransactionalManagerConfig {

    @Bean("platformTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("masterTransaction") DataSourceTransactionManager master,
                                                                 @Qualifier("severntTransaction") DataSourceTransactionManager severnt){
        return new ChainedTransactionManager(master, severnt);
    }

}
