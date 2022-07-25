package vn.trinhtung.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = ValidImageConstraint.class)
public @interface ValidImage {
	public String message() default "Hình ảnh không hợp lệ";

	public Class<?>[] groups() default {};

	public Class<? extends Payload>[] payload() default {};
}
