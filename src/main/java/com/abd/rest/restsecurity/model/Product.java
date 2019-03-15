package com.abd.rest.restsecurity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Product {
    @Id
    @SequenceGenerator(name = "SEQ_PRODUCT_ID", sequenceName = "SEQ_PRODUCT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCT_ID")
    private Long id;
    private String prodName;
    private String prodDesc;
    private Double prodPrice;
    private String prodImage;
}
