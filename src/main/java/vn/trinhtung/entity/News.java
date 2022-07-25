package vn.trinhtung.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.trinhtung.validator.ValidImage;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Indexed
@Entity
@EntityListeners(AuditingEntityListener.class)
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Field
	@NotEmpty(message = "Tiêu đề không được để trống")
	@Column(unique = true, nullable = false)
	private String title;

	@Field
	@NotEmpty(message = "Nội dung tóm tắt không được để trống")
	@Column(nullable = false, columnDefinition = "text")
	private String description;

	@Field
	@NotEmpty(message = "Nội dung không được để trống")
	@Column(nullable = false, columnDefinition = "text")
	private String content;

	@NotEmpty(message = "Slug không được để trống")
	@Column(unique = true, nullable = false)
	private String slug;

	private boolean enable;

	private String image;

	@IndexedEmbedded
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Review> reviews;

	@OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Comment> comments;

	@ValidImage
	@Transient
	private MultipartFile imageFile;

	@CreatedDate
	private Date createdDate;

	public String getImageUrl() {
		return "https://res.cloudinary.com/tung071201/image/upload/v1651760535/" + image;
	}

	public News(Integer id) {
		super();
		this.id = id;
	}
}
