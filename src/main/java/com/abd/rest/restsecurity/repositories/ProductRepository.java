package com.abd.rest.restsecurity.repositories;

import com.abd.rest.restsecurity.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
