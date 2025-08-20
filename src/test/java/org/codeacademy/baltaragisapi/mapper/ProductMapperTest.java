package org.codeacademy.baltaragisapi.mapper;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapperImpl();

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


