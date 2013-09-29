package pw.ry4n.comments.webapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import pw.ry4n.comments.data.model.Author;

/**
 * Utility class for interacting with the {@link SecurityContext}.
 * 
 * @author Ryan Powell
 */
public class SpringSecurityUtils {
	protected static final Logger logger = LoggerFactory
			.getLogger(SpringSecurityUtils.class);

	private SpringSecurityUtils() {
		// prevent instantiation of utility class
	}

	/**
	 * @return the {@link UserDetails} object for the currently logged in user,
	 *         or {@code null} if no one is logged in
	 */
	public static Author getLoggedInAuthor() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication != null
				&& Author.class.isAssignableFrom(authentication.getPrincipal()
						.getClass())) {
			return (Author) authentication.getPrincipal();
		}
		return null;
	}
}
