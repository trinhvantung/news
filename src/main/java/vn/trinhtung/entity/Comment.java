package vn.trinhtung.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Length(min = 1, max = 255)
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "news_id")
	private News news;

	@JsonIgnore
	@OneToMany(mappedBy = "parent")
	private List<Comment> commentReplies;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Comment parent;

	@CreatedDate
	private Date createdDate;

	@Transient
	private Long countReply;

	public Comment(Long id, String content, User user, News news, List<Comment> commentReplies, Comment parent) {
		super();
		this.id = id;
		this.content = content;
		this.user = user;
		this.news = news;
		this.commentReplies = commentReplies;
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", news=" + news + ", countReply=" + countReply + "]";
	}

	public Comment(Long id) {
		super();
		this.id = id;
	}

}
