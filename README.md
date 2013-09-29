# java-comments

A comment system supporting multiple websites with a RESTful API and OpenID verification. This project is written as a Java webapp using Spring. Inspiration for this project came from [using Disqus for comments on a Ghost blog](http://blog.christophvoigt.com/enable-comments-on-ghost-with-disqus/). I thought the idea of separating comments from your blog/website/platform was great, but wanted an alternative that did not rely on a third-party service, so I created java-comments.

_This project is currently under design/development&mdash;the API, code, and concept are not final._

## REST API

The REST API design was based on the [WordPress Comment API](http://developer.wordpress.com/docs/api/). The API was adapted to better conform to REST standards (e.g. PUT for edit) and to ensure naming consistency (e.g. comments vs replies).

<table>
  <th>Resource</th>
  <th>Description</th>
  <tr>
    <td>GET /$site/comments/</td>
    <td>Return recent Comments</td>
  </tr>
  <tr>
    <td>GET /$site/comments/$comment_id</td>
    <td>Return a single Comment</td>
  </tr>
  <tr>
    <td>PUT /$site/comments/$comment_id</td>
    <td>Edit a Comment</td>
  </tr>
  <tr>
    <td>DELETE /$site/comments/$comment_id</td>
    <td>Delete a Comment (and any replies)</td>
  </tr>
  <tr>
    <td>GET /$site/posts/$post_id/comments/</td>
    <td>Return recent Comments for a Post</td>
  </tr>
  <tr>
    <td>POST /$site/posts/$post_id/comments</td>
    <td>Create a Comment on a Post</td>
  </tr>
  <tr>
    <td>GET /$site/comments/$comment_id/replies/</td>
    <td>Return all replies to a single Comment</td>
  </tr>
  <tr>
    <td>POST /$site/comments/$comment_id/replies</td>
    <td>Create a Comment as a reply to another Comment</td>
  </tr>
</table>

