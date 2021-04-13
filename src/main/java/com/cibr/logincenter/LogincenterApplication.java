package com.cibr.logincenter;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCasClient
@SpringBootApplication
@MapperScan("com.cibr.logincenter.dao")
public class LogincenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogincenterApplication.class, args);
    }

}
