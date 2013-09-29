package pw.ry4n.comments.webapp.controller;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pw.ry4n.comments.data.CommentRepository;
import pw.ry4n.comments.data.model.Comment;
import pw.ry4n.comments.webapp.security.SpringSecurityUtils;

@Controller
public class CommentController {
	@Value("${allowedSites}")
	Set<String> allowedSites;

	@Inject
	private CommentRepository commentRepository;

	@RequestMapping(value = "/{siteName}/comments", method = RequestMethod.GET)
	@ResponseBody
	public List<Comment> getCommentsBySite(
			@PathVariable("siteName") String siteName) {
		return commentRepository
				.findBySiteNameOrderByCreateDtDesc(siteName);
	}

	@RequestMapping(value = "/{siteName}/comments/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Comment getCommentById(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id) {
		return commentRepository.findOne(id);
	}

	@RequestMapping(value = "/{siteName}/comments/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void replaceComment(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id,
			@ModelAttribute("comment") Comment comment,
			HttpServletRequest request) {
		// TODO make sure authors are the same, or ROLE_ADMIN

		// only allow update of comment content
		Comment toUpdate = commentRepository.findOne(id);
		toUpdate.setContent(comment.getContent());
		toUpdate.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(toUpdate);
	}

	@RequestMapping(value = "/{siteName}/comments/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteComment(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id) {
		// TODO make sure authors are the same, or ROLE_ADMIN
		commentRepository.delete(id);
	}

	@RequestMapping(value = "/{siteName}/posts/{postId}/comments", method = RequestMethod.GET)
	@ResponseBody
	public List<Comment> getCommentsByPost(
			@PathVariable("siteName") String siteName,
			@PathVariable("postId") String postId) {
		return commentRepository
				.findBySiteNameAndPostIdOrderByCreateDtDesc(siteName,
						postId);
	}

	@RequestMapping(value = "/{siteName}/posts/{postId}/comments", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void createComment(@PathVariable("siteName") String siteName,
			@PathVariable("postId") String postId,
			@ModelAttribute("comment") Comment comment,
			HttpServletRequest request) {
		comment.setPostId(postId);
		comment.setSiteName(siteName);
		comment.setAuthor(SpringSecurityUtils.getLoggedInAuthor());
		comment.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(comment);
	}

	@RequestMapping(value = "/{siteName}/comments/{commentId}/replies", method = RequestMethod.GET)
	@ResponseBody
	public List<Comment> getRepliesForComment(
			@PathVariable("siteName") String siteName,
			@PathVariable("commentId") Long parentCommentId) {
		return commentRepository
				.findBySiteNameAndParentIdOrderByCreateDtDesc(siteName,
						parentCommentId);
	}

	@RequestMapping(value = "/{siteName}/comments/{commentId}/replies", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void createReply(@PathVariable("siteName") String siteName,
			@PathVariable("id") Long id,
			@PathVariable("commentId") Long parentCommentId,
			@ModelAttribute("comment") Comment comment,
			HttpServletRequest request) {
		Comment parent = commentRepository.findOne(parentCommentId);
		comment.setParent(parent);
		comment.setSiteName(siteName);
		comment.setAuthor(SpringSecurityUtils.getLoggedInAuthor());
		comment.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(comment);
	}
}
