package pw.ry4n.comments.data.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "COMMENTS")
public class Comment {
	private Long id;
	private String postId;
	private String siteName;
	private Author author;
	private String content;
	private String authorIP;
	private Date createDt;
	private Date editDt;
	private Comment parent;
	private List<Comment> children;
	private int version;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CommentSequence")
	@SequenceGenerator(name = "CommentSequence", sequenceName = "CommentSequence")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "POST_ID", nullable = false)
	@XmlTransient
	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	@XmlTransient
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID", nullable = true)
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	@Column(length = 4000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlTransient
	public String getAuthorIP() {
		return authorIP;
	}

	public void setAuthorIP(String authorIP) {
		this.authorIP = authorIP;
	}

	@Column(nullable = false)
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getEditDt() {
		return editDt;
	}

	public void setEditDt(Date editDt) {
		this.editDt = editDt;
	}

	@ManyToOne
	@JoinColumn(name = "PARENT_COMMENT_ID", nullable = true)
	@XmlTransient
	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", cascade = {}, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("createDt DESC")
	public List<Comment> getChildren() {
		return children;
	}

	public void setChildren(List<Comment> children) {
		this.children = children;
	}

	@Version
	@XmlTransient
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@PrePersist
	void createdAt() {
		this.createDt = new Date();
	}

	@PreUpdate
	void updatedAt() {
		this.editDt = new Date();
	}
}
