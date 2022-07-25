package vn.trinhtung.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.UserRequest;
import vn.trinhtung.entity.User;
import vn.trinhtung.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserRequestValidator implements Validator {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UserRequest.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserRequest user = (UserRequest) target;
		if (user.getId() == null) {
			if (!user.getPassword().matches("[A-Za-z0-9]{5,30}")) {
				errors.rejectValue("password", "password",
						"Mật khẩu có độ dài từ 5 đến 30 ký tự và chỉ chứa số và chữ cái");
			}
			if (userRepository.findByEmail(user.getEmail()).isPresent()) {
				errors.rejectValue("email", "email", "Email đã tồn tại");
			}
		} else {
			if (user.getPassword() != null && user.getNewPassword() != null) {
				if (!user.getPassword().trim().isEmpty()) {
					User savedUser = userRepository.findById(user.getId()).get();
					if (!passwordEncoder.matches(user.getPassword(), savedUser.getPassword())) {
						errors.rejectValue("password", "password", "Mật khẩu không chính xác");
					}
				}
				if (user.getPassword().trim().isEmpty()) {
					errors.rejectValue("password", "password",
							"Mật khẩu có độ dài từ 5 đến 30 ký tự và chỉ chứa số và chữ cái");
				}
			}
			if (user.getNewPassword() != null && !user.getNewPassword().matches("[A-Za-z0-9]{5,30}")) {
				errors.rejectValue("newPassword", "newPassword",
						"Mật khẩu có độ dài từ 5 đến 30 ký tự và chỉ chứa số và chữ cái");
			}

		}
	}

}
