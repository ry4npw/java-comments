package pw.ry4n.comments.aspect;

import static org.mockito.Mockito.*;

import org.aspectj.lang.JoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SiteWhitelistAspectTest {
	@Mock
	protected JoinPoint joinPoint;

	protected SiteWhitelistAspect aspect = new SiteWhitelistAspect();

	@Test
	public void testBeforeMethodAllowsWhitelistedSite() throws Throwable {
		Object[] args = new Object[]{"good.com", 3};
		when(joinPoint.getArgs()).thenReturn(args);

		aspect.enabled = true;
		aspect.whitelistedSiteArray = new String[]{"good.com"};
		aspect.afterPropertiesSet();

		aspect.beforeMethod(joinPoint);

		verify(joinPoint, times(1)).getArgs();
	}

	@Test
	public void testBeforeMethodDoesNotRunWhenDisabled() throws Throwable {
		Object[] args = new Object[]{"good.com", 3};
		when(joinPoint.getArgs()).thenReturn(args);

		aspect.enabled = false;
		aspect.whitelistedSiteArray = new String[]{"good.com"};
		aspect.afterPropertiesSet();

		aspect.beforeMethod(joinPoint);

		verify(joinPoint, times(0)).getArgs();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBeforeMethodThrowsIllegalArgumentException() throws Throwable {
		Object[] args = new Object[]{"bad.com", 3};
		when(joinPoint.getArgs()).thenReturn(args);

		aspect.enabled = true;

		aspect.beforeMethod(joinPoint);
	}
}
