package ec.uteq.agrocadena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Punto de entrada del backend de AgroCadena. */
@SpringBootApplication
@EnableScheduling
public class AgroCadenaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgroCadenaApplication.class, args);
	}
}
