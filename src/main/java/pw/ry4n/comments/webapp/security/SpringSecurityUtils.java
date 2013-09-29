package pw.ry4n.comments.webapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pw.ry4n.comments.data.model.Author;

public class SpringSecurityUtils {
	protected static final Logger logger = LoggerFactory
			.getLogger(SpringSecurityUtils.class);

	private SpringSecurityUtils() {
		// prevent instantiation
	}

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
