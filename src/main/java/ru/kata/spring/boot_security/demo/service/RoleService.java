package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    @Transactional
    public Role findOrCreateRole(String name) {
        Role role = findByName(name);
        if (role == null) {
            role = new Role(name);
            save(role);
        }
        return role;
    }
    @Transactional
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
