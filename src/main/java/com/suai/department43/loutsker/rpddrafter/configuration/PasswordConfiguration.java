package com.suai.department43.loutsker.rpddrafter.configuration;

import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Account;
import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Role;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Person;
import com.suai.department43.loutsker.rpddrafter.repository.auth.AccountRepository;
import com.suai.department43.loutsker.rpddrafter.repository.auth.RoleRepository;
import com.suai.department43.loutsker.rpddrafter.repository.business.PersonRepository;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class PasswordConfiguration {
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

    public PasswordConfiguration() {
        passwordEncoder = new BCryptPasswordEncoder();
        passwordGenerator = new PasswordGenerator();
    }

    @Bean
    CommandLineRunner initAdmin(AccountRepository accRepo,
                                @Value("${app.admin.name}") String adminName,
                                @Value("${app.admin.password}") String adminPassword,
                                PersonRepository personRepo, RoleRepository roleRepo) {
        return args -> {
            if (accRepo.findAccountByLogin(adminName).isEmpty()) {
                Person person = new Person();
                person.setName(adminName);
                person.setLastname("Admin");
                person.setPatronymic("");
                person.setEmail("");

                personRepo.save(person);

                Optional<Role> roleAdmin = roleRepo.findByName("ADMIN");

                String passwordHash = passwordEncoder.encode(adminPassword);

                Account account = new Account();
                account.setBearer(person);
                account.setLogin(adminName);
                List<Role> roles = new ArrayList<>();
                roles.add(roleAdmin.get());
                account.setRoles(roles);
                account.setPassword(passwordHash);

                accRepo.save(account);
            }
        };
    }

    @Bean
    public PasswordGenerator passwordGenerator() {
        return passwordGenerator;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
