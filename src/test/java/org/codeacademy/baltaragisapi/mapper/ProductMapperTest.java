package org.codeacademy.baltaragisapi.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper mapper;

    @Test
    void centsToDecimal_and_back_isLocaleAgnostic() {
        String dec = mapper.toDecimalString(4500);
        assertThat(dec).isEqualTo("45.00");

        Integer cents = mapper.toCents("45.00");
        assertThat(cents).isEqualTo(4500);

        Integer cents2 = mapper.toCents("45,00");
        assertThat(cents2).isEqualTo(4500);
    }
}


