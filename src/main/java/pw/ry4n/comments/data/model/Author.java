package pw.ry4n.comments.data.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Entity
@Table(name = "AUTHORS")
public class Author extends User {
	private static final long serialVersionUID = -4700123203366357992L;

	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private List<Comment> comments;
	private Long version;

	/**
	 * Default constructor
	 */
	public Author() {
		super("unused", "unused", Collections.<GrantedAuthority> emptyList());
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param username
	 * @param authorities
	 */
	public Author(String username,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, "unused", authorities);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuthorSequence")
	@SequenceGenerator(name = "AuthorSequence", sequenceName = "AuthorSequence")
	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "USERNAME")
	@Override
	public String getUsername() {
		if (username == null) {
			return super.getUsername();
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * First name is not provided by all OpenID providers. Use
	 * {@link #getFullName()} as a backup.
	 * 
	 * @return the first name of the author
	 */
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Last name is not provided by all OpenID providers. Use
	 * {@link #getFullName())} as a backup.
	 * 
	 * @return the last name of the author
	 */
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the full name of the author
	 */
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the email address of the author
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(mappedBy = "author", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("createDt DESC")
	@XmlTransient
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Version
	@XmlTransient
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
