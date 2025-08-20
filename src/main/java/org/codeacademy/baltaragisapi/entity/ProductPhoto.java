package org.codeacademy.baltaragisapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_photo")
public class ProductPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String url;

    @Column
    private String alt;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column
    private Integer width;

    @Column
    private Integer height;

}


