package pw.ry4n.comments.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import pw.ry4n.comments.data.AuthorRepository;
import pw.ry4n.comments.data.model.Author;

/**
 * Custom UserDetailsService which accepts any OpenID user, "registering" new
 * users in a map so they can be welcomed back to the site on subsequent logins.
 */
@Service
public class AuthorDetailsService implements UserDetailsService,
		AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
	@Inject
	private AuthorRepository authorRepository;

	private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = AuthorityUtils
			.createAuthorityList("ROLE_USER");

	/**
	 * Implementation of {link UserDetailsService} that satisfies the
	 * {@code RememberMeServices} requirements.
	 */
	public UserDetails loadUserByUsername(String id)
			throws UsernameNotFoundException {
		UserDetails user = authorRepository.findByUsername(id);

		if (user == null) {
			throw new UsernameNotFoundException(id);
		}

		return user;
	}

	/**
	 * Implementation of {@code AuthenticationUserDetailsService} which allows
	 * full access to the submitted {@code Authentication} object. Used by the
	 * OpenIDAuthenticationProvider.
	 */
	public UserDetails loadUserDetails(OpenIDAuthenticationToken token) {
		String id = token.getIdentityUrl();

		Author user = authorRepository.findByUsername(id);

		if (user != null) {
			return user;
		}

		String email = null;
		String firstName = null;
		String lastName = null;
		String fullName = null;

		List<OpenIDAttribute> attributes = token.getAttributes();

		for (OpenIDAttribute attribute : attributes) {
			switch (attribute.getName()) {
			case "email":
				email = attribute.getValues().get(0);
				break;
			case "firstname":
				firstName = attribute.getValues().get(0);
				break;
			case "lastname":
				lastName = attribute.getValues().get(0);
				break;
			case "fullname":
				fullName = attribute.getValues().get(0);
				break;
			}
		}

		if (fullName == null) {
			StringBuilder fullNameBldr = new StringBuilder();

			if (firstName != null) {
				fullNameBldr.append(firstName);
			}

			if (lastName != null) {
				fullNameBldr.append(" ").append(lastName);
			}
			fullName = fullNameBldr.toString();
		}

		user = new Author(id, DEFAULT_AUTHORITIES);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setFullName(fullName);

		return authorRepository.save(user);
	}
}
