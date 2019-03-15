package com.abd.rest.restsecurity.repositories;

import com.abd.rest.restsecurity.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
