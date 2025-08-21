package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.spec.ProductSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TestEntityManager entityManager;

	@BeforeEach
	void setUp() {
		OffsetDateTime now = OffsetDateTime.now();
		
		// Create test products
		Product publishedProduct = new Product();
		publishedProduct.setName("Test Print Product");
		publishedProduct.setSlug("test-print");
		publishedProduct.setShortDesc("A test print");
		publishedProduct.setLongDesc("A longer description");
		publishedProduct.setPriceCents(2500);
		publishedProduct.setCurrency("EUR");
		publishedProduct.setQuantity(5);
		publishedProduct.setIsPublished(true);
		publishedProduct.setCreatedAt(now);
		publishedProduct.setUpdatedAt(now);
		entityManager.persist(publishedProduct);

		Product unpublishedProduct = new Product();
		unpublishedProduct.setName("Unpublished Product");
		unpublishedProduct.setSlug("unpublished");
		unpublishedProduct.setShortDesc("Not visible");
		unpublishedProduct.setLongDesc("Should not appear");
		unpublishedProduct.setPriceCents(1000);
		unpublishedProduct.setCurrency("EUR");
		unpublishedProduct.setQuantity(1);
		unpublishedProduct.setIsPublished(false);
		unpublishedProduct.setCreatedAt(now);
		unpublishedProduct.setUpdatedAt(now);
		entityManager.persist(unpublishedProduct);

		entityManager.flush();
	}

	@Test
	void findPublishedPaged_and_specByQuery_compose() {
		// Test basic published filter
		Page<Product> page = productRepository.findAll(ProductSpecifications.isPublished(), PageRequest.of(0, 10));
		assertThat(page).isNotNull();
		assertThat(page.getContent()).hasSize(1);
		assertThat(page.getContent().get(0).getIsPublished()).isTrue();

		// Test combined specification with query filter  
		Specification<Product> spec = ProductSpecifications.isPublished()
			.and(ProductSpecifications.byQuery("print"));
		Page<Product> filtered = productRepository.findAll(spec, PageRequest.of(0, 10));
		assertThat(filtered).isNotNull();
		assertThat(filtered.getContent()).hasSize(1);
		assertThat(filtered.getContent().get(0).getName()).contains("Print");
	}
}
