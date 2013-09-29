package pw.ry4n.comments.webapp.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(2)
public class SecurityWebAppInitializer extends
		AbstractSecurityWebApplicationInitializer {

}
