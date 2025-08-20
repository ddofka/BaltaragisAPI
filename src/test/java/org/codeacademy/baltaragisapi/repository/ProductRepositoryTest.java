package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.spec.ProductSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	void findPublishedPaged_and_specByQuery_compose() {
		Page<Product> page = productRepository.findAll(ProductSpecifications.isPublished(), PageRequest.of(0, 10));
		assertThat(page).isNotNull();

		Specification<Product> spec = Specification.where(ProductSpecifications.isPublished())
			.and(ProductSpecifications.byQuery("print"));
		Page<Product> filtered = productRepository.findAll(spec, PageRequest.of(0, 10));
		assertThat(filtered).isNotNull();
	}
}
