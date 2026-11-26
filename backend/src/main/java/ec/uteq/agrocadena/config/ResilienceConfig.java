// backend/src/main/java/ec/uteq/agrocadena/config/ResilienceConfig.java
package ec.uteq.agrocadena.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

  @Bean
  public CircuitBreakerRegistry circuitBreakerRegistry() {
    CircuitBreakerConfig cfg = CircuitBreakerConfig.custom()
      .failureRateThreshold(50) // 50 % errores -> abre
      .slowCallDurationThreshold(Duration.ofSeconds(2))
      .waitDurationInOpenState(Duration.ofSeconds(15))
      .permittedNumberOfCallsInHalfOpenState(5)
      .slidingWindowSize(20)
      .build();
    return CircuitBreakerRegistry.of(cfg);
  }
}
