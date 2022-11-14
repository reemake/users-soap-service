package ru.codemark.userssoapservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.codemark.userssoapservice.entity.Role;
import ru.codemark.userssoapservice.entity.User;
import ru.codemark.userssoapservice.repository.RoleRepository;
import ru.codemark.userssoapservice.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public void run(ApplicationArguments args) {
        roleRepository.save(new Role(1L, "Admin", null));
        roleRepository.save(new Role(2L, "Operator", null));
        roleRepository.save(new Role(3L, "Analyst", null));
//
//        Set<Role> roles = new HashSet<>();
//        roles.add(roleRepository.findById(1L).get());
//        roles.add(roleRepository.findById(3L).get());
//        userRepository.save(new User("reemake", "Nikita", "1234", roles));
//        userRepository.save(new User("captoshka", "Anton", "cap333", roles));
    }
}
