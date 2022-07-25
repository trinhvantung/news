package vn.trinhtung.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class ValidImageConstraint implements ConstraintValidator<ValidImage, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if (value != null && !value.getOriginalFilename().trim().isEmpty()) {
			return isValidImage(value.getContentType());
		}
		return true;
	}

	private boolean isValidImage(String contentType) {
		return contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/jpeg");
	}

}
