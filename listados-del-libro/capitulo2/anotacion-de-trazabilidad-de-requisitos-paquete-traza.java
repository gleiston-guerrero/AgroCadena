package ec.uteq.agrocadena.traza;

import java.lang.annotation.*;

/** Asocia un artefacto (clase o metodo) con uno o varios requisitos. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Requisito {
	/** Identificadores del cap. 1, p. ej. {"RF-01","RNF-03"}. */
	String[] value();
}
