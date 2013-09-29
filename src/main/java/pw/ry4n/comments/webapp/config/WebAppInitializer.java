package pw.ry4n.comments.webapp.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.wink.server.internal.servlet.RestServlet;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Order(1)
/**
 * Java configuration used in Servlet 3.0 instances over the traditional
 * "web.xml".
 * 
 * @author Ryan Powell
 */
public class WebAppInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

		// find and register all @Configuration classes
		rootContext.scan("pw.ry4n.comments.webapp.config");

		// Manage the lifecycle of the root application context
		servletContext.addListener(new ContextLoaderListener(rootContext));

		// Wink configuration
		ServletRegistration.Dynamic winkServlet = servletContext.addServlet(
				"Wink REST Servlet", new RestServlet());
		winkServlet.setLoadOnStartup(1);
		winkServlet.addMapping("/*");
	}
}
