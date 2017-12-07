package com.ng.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * Created by natalia on 23/11/17.
 */

@SpringBootApplication
@EnableDiscoveryClient
@Import(UsersConfiguration.class)
public class UsersServer {

//    @Autowired
//    protected UsersRepository usersRepository;
//
//    protected Logger logger = Logger.getLogger(UsersServer.class.getName());

    /**
     * Run the application using Spring Boot and an embedded servlet engine.
     *
     * @param args
     *            Program arguments - ignored.
     */
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "users-server");

        SpringApplication.run(UsersServer.class, args);
    }
}

