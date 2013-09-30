package pw.ry4n.comments.webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import pw.ry4n.comments.service.AuthorDetailsService;

/**
 * Spring Security configuration.
 * 
 * @author Ryan Powell
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			// disable CSRF for now...
			.csrf()
				.disable()
			.authorizeRequests()
				// allow all GET requests
				.antMatchers(HttpMethod.GET, "/**").permitAll()
				// secure PUT, POST, DELETE, etc
				.anyRequest().permitAll()
				.and()
			.openidLogin()
				.permitAll()
				.authenticationUserDetailsService(new AuthorDetailsService())
				.attributeExchange("https://www.google.com/.*")
					.attribute("email")
						.type("http://axschema.org/contact/email")
						.required(true)
						.and()
					.attribute("firstname")
						.type("http://axschema.org/namePerson/first")
						.required(true)
						.and()
					.attribute("lastname")
						.type("http://axschema.org/namePerson/last")
						.required(true)
						.and()
					.and()
				.attributeExchange(".*yahoo.com.*")
					.attribute("email")
						.type("http://axschema.org/contact/email")
						.required(true)
						.and()
					.attribute("fullname")
						.type("http://axschema.org/namePerson")
						.required(true)
						.and()
					.and()
				.attributeExchange(".*myopenid.com.*")
					.attribute("email")
						.type("http://schema.openid.net/contact/email")
						.required(true)
						.and()
					.attribute("fullname")
						.type("http://schema.openid.net/namePerson")
						.required(true);
		// @formatter:on
	}
}
