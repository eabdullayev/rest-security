package com.abd.rest.restsecurity.repositories;

import com.abd.rest.restsecurity.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}
