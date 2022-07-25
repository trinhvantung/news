package vn.trinhtung.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.entity.Category;
import vn.trinhtung.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class CategoryValidator implements Validator {
	private final CategoryRepository categoryRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(Category.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Category category = (Category) target;
		if (category.getId() == null) {
			if (categoryRepository.findByNameIgnoreCase(category.getName().trim()) != null) {
				errors.rejectValue("name", "name", "Tên danh mục đã tồn tại");
			}
			if (categoryRepository.findBySlugIgnoreCase(category.getSlug().trim()) != null) {
				errors.rejectValue("slug", "slug", "Slug đã tồn tại");
			}
		} else {
			Category categoryByName = categoryRepository.findByNameIgnoreCase(category.getName().trim());
			Category categoryBySlug = categoryRepository.findBySlugIgnoreCase(category.getSlug().trim());

			if (categoryByName != null && categoryByName.getId() != category.getId()) {
				errors.rejectValue("name", "name", "Tên danh mục đã tồn tại");
			}
			if (categoryBySlug != null && categoryBySlug.getId() != category.getId()) {
				errors.rejectValue("slug", "slug", "Slug đã tồn tại");
			}
		}
	}

}
