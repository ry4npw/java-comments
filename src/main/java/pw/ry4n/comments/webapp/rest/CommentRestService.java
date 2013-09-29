package pw.ry4n.comments.webapp.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import pw.ry4n.comments.data.CommentRepository;
import pw.ry4n.comments.data.model.Comment;
import pw.ry4n.comments.webapp.security.SpringSecurityUtils;

@Service
@Produces("application/json")
public class CommentRestService {
	@Inject
	private CommentRepository commentRepository;

	@GET
	@Path(value = "/{siteName}/comments")
	public Response getCommentsBySite(@PathParam("siteName") String siteName) {
		return Response.ok(
				commentRepository.findBySiteNameOrderByCreateDtDesc(siteName))
				.build();
	}

	@GET
	@Path(value = "/{siteName}/comments/{id}")
	public Comment getCommentById(@PathParam("siteName") String siteName,
			@PathParam("id") Long id) {
		return commentRepository.findOne(id);
	}

	@PUT
	@Consumes("application/json")
	@Path(value = "/{siteName}/comments/{id}")
	public void replaceComment(@PathParam("siteName") String siteName,
			@PathParam("id") Long id, Comment comment,
			@Context HttpServletRequest request) {
		// TODO make sure authors are the same, or ROLE_ADMIN

		// only allow update of comment content
		Comment toUpdate = commentRepository.findOne(id);
		toUpdate.setContent(comment.getContent());
		toUpdate.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(toUpdate);
	}

	@DELETE
	@Path(value = "/{siteName}/comments/{id}")
	public Response deleteComment(@PathParam("siteName") String siteName,
			@PathParam("id") Long id) {
		// TODO make sure authors are the same, or ROLE_ADMIN
		commentRepository.delete(id);
		return Response.ok().build();
	}

	@GET
	@Path(value = "/{siteName}/posts/{postId}/comments")
	public Response getCommentsByPost(@PathParam("siteName") String siteName,
			@PathParam("postId") String postId) {
		return Response.ok(
				commentRepository.findBySiteNameAndPostIdOrderByCreateDtDesc(
						siteName, postId)).build();
	}

	@POST
	@Consumes("application/json")
	@Path(value = "/{siteName}/posts/{postId}/comments")
	public Response createComment(@PathParam("siteName") String siteName,
			@PathParam("postId") String postId, Comment comment,
			@Context HttpServletRequest request) {
		comment.setPostId(postId);
		comment.setSiteName(siteName);
		comment.setAuthor(SpringSecurityUtils.getLoggedInAuthor());
		comment.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(comment);
		return Response.ok().build();
	}

	@GET
	@Path(value = "/{siteName}/comments/{commentId}/replies")
	public Response getRepliesForComment(
			@PathParam("siteName") String siteName,
			@PathParam("commentId") Long parentCommentId) {
		return Response.ok(
				commentRepository.findBySiteNameAndParentIdOrderByCreateDtDesc(
						siteName, parentCommentId)).build();
	}

	@POST
	@Consumes("application/json")
	@Path(value = "/{siteName}/comments/{commentId}/replies")
	public Response createReply(@PathParam("siteName") String siteName,
			@PathParam("commentId") Long parentCommentId, Comment comment,
			@Context HttpServletRequest request) {
		Comment parent = commentRepository.findOne(parentCommentId);
		comment.setParent(parent);
		comment.setSiteName(siteName);
		comment.setAuthor(SpringSecurityUtils.getLoggedInAuthor());
		comment.setAuthorIP(request.getRemoteAddr());
		commentRepository.save(comment);
		return Response.ok().build();
	}
}
