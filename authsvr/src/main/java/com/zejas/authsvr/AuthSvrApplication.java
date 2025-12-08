package com.zejas.authsvr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAspectJAutoProxy
@MapperScan("com.zejas.authsvr.mapper")
@SpringBootApplication
public class AuthSvrApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AuthSvrApplication.class);
        System.setProperty("nacos.logging.default.config.enabled","false");
        application.run(args);
    }

}
