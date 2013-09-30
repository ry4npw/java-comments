package pw.ry4n.comments.webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration specific to the Spring MVC web application. This class was
 * separated from the {@link AppConfig} and {@link DatabaseConfig} to support
 * unit testing without a servlet.
 * 
 * For the Spring Security configuration, see {@link SecurityConfig}.
 * 
 * @author Ryan Powell
 */
@Configuration
@EnableWebMvc
public class WebAppConfig {

}
