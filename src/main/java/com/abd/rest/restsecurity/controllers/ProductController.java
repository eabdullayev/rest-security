package com.abd.rest.restsecurity.controllers;

import com.abd.rest.restsecurity.model.Product;
import com.abd.rest.restsecurity.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @RequestMapping(method=RequestMethod.GET, value="/api/products")
    public Iterable<Product> product() {
        return productRepository.findAll();
    }

    @RequestMapping(method=RequestMethod.POST, value="/api/products")
    public Long save(@RequestBody Product product) {
        productRepository.save(product);

        return product.getId();
    }

    @RequestMapping(method= RequestMethod.GET, value="/api/products/{id}")
    public Optional<Product> show(@PathVariable Long id) {
        return productRepository.findById(id);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/api/products/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            if (product.getProdName() != null)
                p.setProdName(product.getProdName());
            if (product.getProdDesc() != null)
                p.setProdDesc(product.getProdDesc());
            if (product.getProdPrice() != null)
                p.setProdPrice(product.getProdPrice());
            if (product.getProdImage() != null)
                p.setProdImage(product.getProdImage());
            productRepository.save(p);
            return p;
        }
        return null;
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/api/products/{id}")
    public String delete(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        productRepository.delete(product.get());

        return "product deleted";
    }
}
