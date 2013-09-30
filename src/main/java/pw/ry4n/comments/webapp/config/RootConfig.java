package pw.ry4n.comments.webapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Root configuration class that scans for all other {@code @Configuration}
 * annotated classes in the same package and loads them.
 * 
 * @author Ryan Powell
 */
@Configuration
@ComponentScan(value = "pw.ry4n.comments.webapp.config", excludeFilters = { @Filter(type = FilterType.ASSIGNABLE_TYPE, value = RootConfig.class) })
public class RootConfig {

}
