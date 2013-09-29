package pw.ry4n.comments.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableWebMvc
@ComponentScan({ "pw.ry4n.comments.webapp" })
public class AppConfig {
	/**
	 * Allow {@code @Value} property injection.
	 * 
	 * @return {@link PropertySourcesPlaceholderConfigurer}
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
