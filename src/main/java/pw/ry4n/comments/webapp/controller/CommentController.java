package pw.ry4n.comments.webapp.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pw.ry4n.comments.data.CommentRepository;
import pw.ry4n.comments.data.model.Author;
import pw.ry4n.comments.data.model.Comment;
import pw.ry4n.comments.webapp.security.SpringSecurityUtils;

@Controller
@RequestMapping("/{siteName}")
public class CommentController {
	protected static final Logger logger = LoggerFactory
			.getLogger(CommentController.class);
	@Inject
	private CommentRepository commentRepository;

	@RequestMapping(value = "/comments", method = RequestMethod.GET)
	public @ResponseBody
	List<Comment> getCommentsBySite(@PathVariable("siteName") String siteName) {
		return commentRepository.findBySiteNameOrderByCreateDtDesc(siteName);
	}

	@RequestMapping(value = "/comments/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Comment getCommentById(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id) {
		return commentRepository.findOne(id);
	}

	@RequestMapping(value = "/comments/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void replaceComment(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id, @RequestBody Comment comment,
			HttpServletRequest request) {
		Comment commentToUpdate = commentRepository.findOne(id);
		validateAuthorMatchesLoggedInUser(commentToUpdate.getAuthor());

		// only allow update of comment content
		commentToUpdate.setContent(comment.getContent());
		commentToUpdate.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(commentToUpdate);
	}

	@RequestMapping(value = "/comments/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteComment(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id) {
		Comment commentToDelete = commentRepository.findOne(id);
		validateAuthorMatchesLoggedInUser(commentToDelete.getAuthor());
		commentRepository.delete(commentToDelete);
	}

	@RequestMapping(value = "/posts/{postId}/comments", method = RequestMethod.GET)
	public @ResponseBody
	List<Comment> getCommentsByPost(@PathVariable("siteName") String siteName,
			@PathVariable("postId") String postId) {
		return commentRepository.findBySiteNameAndPostIdOrderByCreateDtDesc(
				siteName, postId);
	}

	@RequestMapping(value = "/posts/{postId}/comments", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void createComment(@PathVariable("siteName") String siteName,
			@PathVariable("postId") String postId,
			@RequestBody Comment comment, HttpServletRequest request) {
		comment.setPostId(postId);
		comment.setSiteName(siteName);
		comment.setAuthor(SpringSecurityUtils.getLoggedInAuthor());
		comment.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(comment);
	}

	@RequestMapping(value = "/comments/{commentId}/replies", method = RequestMethod.GET)
	public @ResponseBody
	List<Comment> getRepliesForComment(
			@PathVariable("siteName") String siteName,
			@PathVariable("commentId") Long parentCommentId) {
		return commentRepository.findBySiteNameAndParentIdOrderByCreateDtDesc(
				siteName, parentCommentId);
	}

	@RequestMapping(value = "/comments/{commentId}/replies", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void createReply(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id,
			@PathVariable("commentId") Long parentCommentId,
			@RequestBody Comment comment, HttpServletRequest request) {
		Comment parent = commentRepository.findOne(parentCommentId);
		comment.setParent(parent);
		comment.setSiteName(siteName);
		comment.setAuthor(SpringSecurityUtils.getLoggedInAuthor());
		comment.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(comment);
	}

	protected void validateAuthorMatchesLoggedInUser(Author author) {
		Author loggedInUser = SpringSecurityUtils.getLoggedInAuthor();
		if (loggedInUser == null || loggedInUser.getUsername() == null) {
			logger.warn("no logged in user, is spring security disabled?");
			return;
		}

		if (author != null
				&& loggedInUser.getUsername().equals(author.getUsername())) {
			throw new SecurityException("You may only edit your own comments");
		}
	}

	@ExceptionHandler(SecurityException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You may only edit or delete your own comments")
	public void handleSecurityException() {
		// empty method
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Site is not registered")
	public void handleException() {
		// empty method
	}
}
