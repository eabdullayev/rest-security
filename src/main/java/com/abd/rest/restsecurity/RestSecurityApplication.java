package com.abd.rest.restsecurity;

import com.abd.rest.restsecurity.model.Role;
import com.abd.rest.restsecurity.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestSecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository) {

        return args -> {

            Role adminRole = roleRepository.findByRole("ADMIN");
            if (adminRole == null) {
                Role newAdminRole = new Role();
                newAdminRole.setRole("ADMIN");
                roleRepository.save(newAdminRole);
            }
        };

    }

}
