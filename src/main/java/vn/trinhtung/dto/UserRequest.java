package vn.trinhtung.dto;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.trinhtung.validator.ValidImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	private Integer id;
	
	@Email(message = "Email không đúng định dạng")
	private String email;

	@Length(min = 5, max = 30, message = "Họ tên có độ dài từ 5 đến 30 ký tự")
	private String fullname;

	private String password;

	private String newPassword;

	@ValidImage
	private MultipartFile imageFile;
}
