package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    @PostConstruct
    @Transactional
    public void init() {


        Role userRole = new Role("USER");
        userRole.setName("USER");
        Role adminRole = new Role("ADMIN");
        adminRole.setName("ADMIN");
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setAge(32);
            user.setSurname("Tihonov");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRolelist(List.of(userRole));

            userRepository.save(user);
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setAge(27);
            admin.setSurname("Tihonova");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRolelist(List.of(adminRole, userRole));
            userRepository.save(admin);
        }
    }

}
