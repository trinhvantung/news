package vn.trinhtung.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import vn.trinhtung.entity.User;
import vn.trinhtung.repository.UserRepository;

@Component
public class UserValidator implements Validator {
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(User.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		if (user.getId() == null) {
			if (!user.getPassword().matches("[A-Za-z0-9]{5,30}")) {
				errors.rejectValue("password", "password",
						"Mật khẩu có độ dài từ 5 đến 30 ký tự và chỉ chứa số và chữ cái");
			}
			if (userRepository.findByEmail(user.getEmail()).isPresent()) {
				errors.rejectValue("email", "email", "Email đã tồn tại");
			}
		} else {
			if (!user.getPassword().trim().isEmpty() && !user.getPassword().matches("[A-Za-z0-9]{5,30}")) {
				errors.rejectValue("password", "password",
						"Mật khẩu có độ dài từ 5 đến 30 ký tự và chỉ chứa số và chữ cái");
			}
			User userByEmail = userRepository.findByEmail(user.getEmail()).orElse(null);
			if (userByEmail != null && userByEmail.getId() != user.getId()) {
				errors.rejectValue("email", "email", "Email đã tồn tại");
			}
		}
	}

}
