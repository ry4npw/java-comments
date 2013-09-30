package pw.ry4n.comments.data;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import pw.ry4n.comments.data.model.Author;

public class AuthorRepositoryTest extends AbstractRepositoryTest {
	@Inject
	protected AuthorRepository repository;

	@Test
	public void testFindByUsernameReturnNull() {
		assertNull(repository.findByUsername(null));
	}

	@Test
	public void testFindByUsernameReturnsUser() {
		Author author = repository.findByUsername("ryan");
		assertNotNull(author);
		assertTrue(author.getId() > 0);
		assertEquals("ryan", author.getUsername());
	}
}
