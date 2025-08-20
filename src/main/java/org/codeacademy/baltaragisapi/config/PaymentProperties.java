package org.codeacademy.baltaragisapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.payments")
public class PaymentProperties {
    
    /**
     * Whether payment functionality is enabled
     * Default: false (disabled)
     */
    private boolean enabled = false;
}
