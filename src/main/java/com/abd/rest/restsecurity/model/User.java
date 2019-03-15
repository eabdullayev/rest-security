package com.abd.rest.restsecurity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames="email"))
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "SEQ_USER_ID", sequenceName = "SEQ_USER_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_ID")
    private Long id;
    private String email;
    private String password;
    private String fullname;
    private boolean enabled;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> roles;
}
