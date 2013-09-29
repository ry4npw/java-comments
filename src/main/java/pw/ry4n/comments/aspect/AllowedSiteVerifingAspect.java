package pw.ry4n.comments.aspect;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AllowedSiteVerifingAspect {
	private static final Logger logger = LoggerFactory
			.getLogger(AllowedSiteVerifingAspect.class);

	@Value("${allowedSites}")
	private String[] allowedSiteArray;
	private Set<String> allowedSites = new HashSet<>();

	@PostConstruct
	public void afterPropertiesSet() {
		if (allowedSiteArray != null) {
			for (String allowedSite : allowedSiteArray) {
				allowedSites.add(allowedSite);
			}
		}
	}

	@Around("execution(* pw.ry4n.comments.webapp.rest.CommentRestService.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] arguments = joinPoint.getArgs();

		if (arguments != null && arguments.length >= 1) {
			String siteName = arguments[0].toString();
			// argument[0] = siteName
			if (!allowedSites.contains(siteName)) {
				logger.debug(siteName + " is not a participating site");
			}
		}

		Object result = joinPoint.proceed();
		logger.debug("returning " + ToStringBuilder.reflectionToString(result));

		return result;
	}
}
