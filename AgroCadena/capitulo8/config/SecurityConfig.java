	// src/main/java/ec/uteq/agrocadena/config/SecurityConfig.java
	package ec.uteq.agrocadena.config;
	
	import ec.uteq.agrocadena.traza.Requisito;
	import org.springframework.context.annotation.*;
	import org.springframework.security.config.annotation.web.builders.HttpSecurity;
	import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
	import org.springframework.security.config.http.SessionCreationPolicy;
	import org.springframework.security.oauth2.jwt.*;
	import org.springframework.security.web.SecurityFilterChain;
	
	@Configuration
	@EnableWebSecurity
	@Requisito({"RNF-03"})
	public class SecurityConfig {
		
		@Bean
		SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(s ->
			s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(a -> a
			.requestMatchers("/actuator/health").permitAll()
			.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated())
			.oauth2ResourceServer(o -> o.jwt(j -> j.decoder(jwtDecoder())))
			.requiresChannel(c -> c.anyRequest().requiresSecure());
			return http.build();
		}
		
		@Bean
		JwtDecoder jwtDecoder() {
			return JwtDecoders.fromIssuerLocation("https://auth.agrocadena.ec");
		}
	}
