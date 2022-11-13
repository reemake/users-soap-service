package ru.codemark.userssoapservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {

    @Id
    @Column(unique = true, name = "login")
    private String login;

    private String name;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
