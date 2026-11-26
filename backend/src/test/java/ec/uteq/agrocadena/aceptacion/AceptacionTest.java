// backend/src/test/java/ec/uteq/agrocadena/aceptacion/AceptacionTest.java
package ec.uteq.agrocadena.aceptacion;

import static io.cucumber.junit.platform.engine.Constants.*;

import org.junit.platform.suite.api.*;

/** Ejecuta los criterios de aceptacion Gherkin de ../features (cap. 6). */
@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(
  key = FEATURES_PROPERTY_NAME,
  value = "../features"
)
@ConfigurationParameter(
  key = GLUE_PROPERTY_NAME,
  value = "ec.uteq.agrocadena.aceptacion"
)
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class AceptacionTest {}
