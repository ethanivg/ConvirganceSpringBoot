package com.invirgance.springbootconvirgance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan("com.invirgance.springbootconvirgance")
@EnableJpaRepositories("com.invirgance.springbootconvirgance")
@ComponentScan("com.invirgance.springbootconvirgance")
@EnableTransactionManagement
public class SpringbootConvirganceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(SpringbootConvirganceApplication.class, args);
    }

}
