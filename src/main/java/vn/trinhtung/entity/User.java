package vn.trinhtung.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.trinhtung.validator.ValidImage;

@Table(name = "`user`")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Email(message = "Email không đúng định dạng")
	@Column(length = 50, nullable = false, unique = true)
	private String email;

	@Length(min = 5, max = 30, message = "Họ tên có độ dài từ 5 đến 30 ký tự")
	@Column(length = 30, nullable = false)
	private String fullname;

	@Column(length = 64, nullable = false)
	private String password;

	private boolean nonLocked;

	private String image;

	@NotEmpty(message = "Chưa chọn chức vụ")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Review> reviews;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Comment> comments;

	@ValidImage
	@Transient
	private MultipartFile imageFile;

	public User(Integer id) {
		super();
		this.id = id;
	}

	public String getImageUrl() {
		if (image == null) {
			return "/user/img/user/user1.svg";
		}
		return "https://res.cloudinary.com/tung071201/image/upload/v1651760535/" + image;
	}

}
