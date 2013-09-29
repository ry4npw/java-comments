package pw.ry4n.comments.data;

import org.springframework.data.jpa.repository.JpaRepository;

import pw.ry4n.comments.data.model.Author;

public interface AuthorRepository extends JpaRepository<Author, String> {
	/**
	 * @param username
	 *            the unique OpenID of the Author
	 * @return the {@link Author} for the provided {@code username}.
	 */
	Author findByUsername(String username);
}
