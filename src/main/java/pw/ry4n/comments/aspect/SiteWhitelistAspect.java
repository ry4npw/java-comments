package pw.ry4n.comments.aspect;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SiteWhitelistAspect {
	private static final Logger logger = LoggerFactory
			.getLogger(SiteWhitelistAspect.class);

	@Value("${sites.whitelist.enable:false}")
	protected boolean enabled = false;
	@Value("${sites.whitelist}")
	protected String[] whitelistedSiteArray = null;

	protected Set<String> whitelist = new HashSet<>();

	@PostConstruct
	public void afterPropertiesSet() {
		if (whitelistedSiteArray != null) {
			for (String allowedSite : whitelistedSiteArray) {
				if (StringUtils.isNotBlank(allowedSite)) {
					whitelist.add(StringUtils.trim(allowedSite));
				}
			}
		}
	}

	@Before("execution(* pw.ry4n.comments.webapp.controller.CommentController.*(..))")
	public void beforeMethod(JoinPoint joinPoint) throws Throwable {
		if (!enabled) {
			return;
		}

		Object[] arguments = joinPoint.getArgs();

		if (arguments != null && arguments.length >= 1) {
			String siteName = arguments[0].toString();
			if (!whitelist.contains(siteName)) {
				logger.error("received request for non-whitelisted site: "
						+ siteName);
				throw new IllegalArgumentException("Site is not registered");
			}
		}
	}
}
