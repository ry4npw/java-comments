package pw.ry4n.comments.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pw.ry4n.comments.data.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findBySiteNameOrderByCreateDtDesc(String siteName);
	List<Comment> findBySiteNameAndPostIdOrderByCreateDtDesc(String siteName, String postId);
	List<Comment> findBySiteNameAndParentIdOrderByCreateDtDesc(String siteName, Long parentCommentId);
}
