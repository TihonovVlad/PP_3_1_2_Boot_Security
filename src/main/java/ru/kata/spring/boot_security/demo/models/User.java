package ru.kata.spring.boot_security.demo.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    Long id;
    @Getter @Setter @NonNull
    @Column(unique = true)
    private String username;
    @Getter @Setter
    @Column
    private String surname;
    @Getter @Setter
    @Column
    private int age;
    @Column
    @NonNull
    @Setter
    private String password;
    @Column
    @Getter @Setter
    private String email;
    @Getter @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles;

    public User() {
    };

    public User(String name, String surname, int age) {
        this.username = name;
        this.surname = surname;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setRolelist(List<Role> roles) {
        this.roles = roles;
    }
}
