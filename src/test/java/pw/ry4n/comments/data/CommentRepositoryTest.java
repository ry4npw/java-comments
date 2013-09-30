package pw.ry4n.comments.data;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import pw.ry4n.comments.data.model.Comment;

public class CommentRepositoryTest extends AbstractRepositoryTest {
	private static final String SITE_NAME = "ry4n.pw";

	@Inject
	protected CommentRepository repository;

	@Test
	public void testFindBySiteNameAndParentId() {
		List<Comment> comments = repository
				.findBySiteNameAndParentIdOrderByCreateDtDesc(SITE_NAME, 1L);
		assertNotNull(comments);
		assertFalse(comments.isEmpty());
		assertEquals(1, comments.size());
	}

	@Test
	public void testFindBySiteNameAndPostId() {
		List<Comment> comments = repository
				.findBySiteNameAndPostIdOrderByCreateDtDesc(SITE_NAME, "1");
		assertNotNull(comments);
		assertFalse(comments.isEmpty());
		assertEquals(2, comments.size());
	}

	@Test
	public void testFindBySiteName() {
		List<Comment> comments = repository
				.findBySiteNameOrderByCreateDtDesc(SITE_NAME);
		assertNotNull(comments);
		assertFalse(comments.isEmpty());
		assertEquals(2, comments.size());
	}
}
