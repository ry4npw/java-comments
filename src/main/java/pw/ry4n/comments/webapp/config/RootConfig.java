package pw.ry4n.comments.webapp.config;

import static org.springframework.context.annotation.ComponentScan.Filter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value = "pw.ry4n.comments.webapp.config", excludeFilters = { @Filter(type = FilterType.ASSIGNABLE_TYPE, value = RootConfig.class) })
public class RootConfig {

}