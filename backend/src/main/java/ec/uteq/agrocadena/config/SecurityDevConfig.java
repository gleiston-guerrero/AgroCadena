package ec.uteq.agrocadena.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

/**
 * Seguridad para desarrollo local (perfil distinto de "prod"): no exige JWT,
 * de modo que el lector pueda probar la API y los clientes sin un servidor de
 * autorizacion. NO usar en produccion.
 */
@Configuration
@EnableWebSecurity
@Profile("!prod")
public class SecurityDevConfig {

	@Bean
	SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable())
			.cors(c -> c.configurationSource(cors()))
			.authorizeHttpRequests(a -> a.anyRequest().permitAll());
		return http.build();
	}

	/** Permite que la app Flutter web y Vite (otro puerto) consulten la API en desarrollo. */
	CorsConfigurationSource cors() {
		CorsConfiguration c = new CorsConfiguration();
		c.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
		c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		c.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
		src.registerCorsConfiguration("/**", c);
		return src;
	}
}
