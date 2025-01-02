package org.xumin.securedoc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.xumin.securedoc.domain.RequestContext;
import org.xumin.securedoc.entity.RoleEntity;
import org.xumin.securedoc.enumeration.Authority;
import org.xumin.securedoc.repository.RoleRepository;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class SecureDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureDocApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {
//            RequestContext.setUserId(0L);
//            var userRole = new RoleEntity();
//            userRole.setName(Authority.USER.name());
//            userRole.setAuthorize(Authority.USER);
//            roleRepository.save(userRole);
//
//            var adminRole = new RoleEntity();
//            adminRole.setName(Authority.ADMIN.name());
//            adminRole.setAuthorize(Authority.ADMIN);
//            roleRepository.save(adminRole);
//
//            RequestContext.start();
        };
    }
}
