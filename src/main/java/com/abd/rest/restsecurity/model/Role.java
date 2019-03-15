package com.abd.rest.restsecurity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames="role"))
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @SequenceGenerator(name = "SEQ_ROLE_ID", sequenceName = "SEQ_ROLE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROLE_ID")
    private Long id;
    private String role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> employees = new HashSet<>();
}
