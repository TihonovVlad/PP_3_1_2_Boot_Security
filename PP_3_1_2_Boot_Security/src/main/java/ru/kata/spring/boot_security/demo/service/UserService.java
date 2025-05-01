package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllWithRoles(); // Специальный метод для загрузки с ролями
    }

    public void saveUser(User user, List<Long> roleIds) {
        List<Role> roles = roleRepository.findAllById(roleIds).stream()
                .collect(Collectors.toList());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Transactional
    public User getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        return userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с ID %d не найден", id)
                ));
    }

    @Transactional
    public void updateUser(Long id, User updatedUser, List<Long> roleIds) {
        User existingUser = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Обновляем только изменяемые поля
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setAge(updatedUser.getAge());

        // Обновляем пароль только если он был изменен
        if (!updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        // Сохраняем без изменения ролей (если нужно обновлять роли - добавьте отдельную логику)
        userRepository.save(existingUser);
    }

    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
//
//    public User getUserById(Long id) {
//         if (id == null) {
//            throw new IllegalArgumentException("ID пользователя не может быть null");
//        }
//        // 2. Поиск пользователя с ролями (используя кастомный метод репозитория)
//        return userRepository.findByIdWithRoles(id)
//                // 3. Обработка случая, когда пользователь не найден
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
//    }
