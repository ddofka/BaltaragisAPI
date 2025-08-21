package org.codeacademy.baltaragisapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {
    
    /**
     * Number of requests allowed per time window
     */
    private int capacity = 10;
    
    /**
     * Time window duration in minutes
     */
    private int windowMinutes = 5;
    
    /**
     * Whether rate limiting is enabled
     */
    private boolean enabled = true;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getWindowMinutes() {
        return windowMinutes;
    }

    public void setWindowMinutes(int windowMinutes) {
        this.windowMinutes = windowMinutes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
