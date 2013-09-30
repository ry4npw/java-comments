package pw.ry4n.comments.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Core Spring Framework configuration.
 * 
 * @author Ryan Powell
 */
@Configuration
@PropertySource({ "classpath:application.properties" })
@ComponentScan(basePackages = { "pw.ry4n.comments" }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = Configuration.class)})
@EnableAspectJAutoProxy
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
