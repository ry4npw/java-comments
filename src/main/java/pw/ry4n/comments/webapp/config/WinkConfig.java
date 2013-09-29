package pw.ry4n.comments.webapp.config;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.wink.spring.Registrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import pw.ry4n.comments.webapp.rest.CommentRestService;

@Configuration
@ImportResource("classpath:META-INF/server/wink-core-context.xml")
public class WinkConfig {
	@Inject
	private CommentRestService commentRestService;

	/**
	 * Register our beans with wink.
	 * 
	 * @return
	 */
	@Bean
	public Registrar registrar() {
		Registrar registrar = new Registrar();
		Set<Object> instances = new HashSet<>();
		instances.add(commentRestService);
		registrar.setInstances(instances);
		return registrar;
	}
}
