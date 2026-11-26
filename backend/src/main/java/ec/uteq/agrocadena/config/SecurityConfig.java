// backend/src/main/java/ec/uteq/agrocadena/config/SecurityConfig.java
package ec.uteq.agrocadena.config;

import ec.uteq.agrocadena.traza.Requisito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("prod")
@Requisito({ "RNF-03" })
public class SecurityConfig {

  @Value("${agrocadena.jwt.issuer}")
  private String emisor;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(s ->
        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(a ->
        a
          .requestMatchers("/actuator/health/**")
          .permitAll()
          .requestMatchers("/actuator/prometheus")
          .permitAll()
          .requestMatchers("/v1/admin/**")
          .hasRole("ADMIN")
          .anyRequest()
          .authenticated()
      )
      .oauth2ResourceServer(o ->
        o.jwt(j ->
          j
            .decoder(jwtDecoder())
            .jwtAuthenticationConverter(rolesDesdeJwt())
        )
      )
      // HTTPS obligatorio para todo trafico que entra por el Ingress
      .requiresChannel(c ->
        c
          .requestMatchers(
            r -> r.getHeader("X-Forwarded-Proto") != null
          )
          .requiresSecure()
      );
    return http.build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return JwtDecoders.fromIssuerLocation(emisor);
  }

  /** Convierte el claim "roles" del token en autoridades ROLE_*. */
  @Bean
  JwtAuthenticationConverter rolesDesdeJwt() {
    JwtGrantedAuthoritiesConverter roles =
      new JwtGrantedAuthoritiesConverter();
    roles.setAuthoritiesClaimName("roles");
    roles.setAuthorityPrefix("ROLE_");
    JwtAuthenticationConverter c = new JwtAuthenticationConverter();
    c.setJwtGrantedAuthoritiesConverter(roles);
    return c;
  }
}
