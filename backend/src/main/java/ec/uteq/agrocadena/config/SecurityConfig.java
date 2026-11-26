// src/main/java/ec/uteq/agrocadena/config/SecurityConfig.java
package ec.uteq.agrocadena.config;

import ec.uteq.agrocadena.traza.Requisito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cadena de filtros del capitulo 8 (lst:agro8-sec). Se activa con el perfil
 * "prod"; el emisor de tokens se configura con agrocadena.jwt.issuer.
 */
@Configuration
@EnableWebSecurity
@Profile("prod")
@Requisito({"RNF-03"})
public class SecurityConfig {
	
	@Value("${agrocadena.jwt.issuer}") String issuer;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.sessionManagement(s ->
		s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(a -> a
		.requestMatchers("/actuator/health/**", "/actuator/prometheus").permitAll()
		.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
		.anyRequest().authenticated())
		.oauth2ResourceServer(o -> o.jwt(j -> j.decoder(jwtDecoder())));
		// TLS se termina en el Ingress del cluster (cap. 9); por eso no se exige
		// requiresSecure() dentro del pod.
		return http.build();
	}
	
	@Bean
	JwtDecoder jwtDecoder() {
		return JwtDecoders.fromIssuerLocation(issuer);
	}
}
