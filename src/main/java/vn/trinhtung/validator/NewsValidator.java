package vn.trinhtung.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.entity.News;
import vn.trinhtung.repository.NewsRepository;

@Component
@RequiredArgsConstructor
public class NewsValidator implements Validator {
	private final NewsRepository newsRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(News.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		News news = (News) target;

		if (news.getId() == null) {
			if (newsRepository.findByTitleIgnoreCase(news.getTitle()) != null) {
				errors.rejectValue("title", "title", "Tiêu đề đã tồn tại");
			}
			if (newsRepository.findBySlugIgnoreCase(news.getSlug()) != null) {
				errors.rejectValue("slug", "slug", "Slug đã tồn tại");
			}
			if (news.getImageFile() == null || news.getImageFile().getOriginalFilename().isEmpty()) {
				errors.rejectValue("imageFile", "imageFile", "Chưa upload ảnh");
			}
		} else {
			News newsByTitle = newsRepository.findByTitleIgnoreCase(news.getTitle());
			if (newsByTitle != null && newsByTitle.getId() != news.getId()) {
				errors.rejectValue("title", "title", "Tiêu đề đã tồn tại");
			}

			News newsBySlug = newsRepository.findBySlugIgnoreCase(news.getSlug());
			if (newsBySlug != null && newsBySlug.getId() != news.getId()) {
				errors.rejectValue("slug", "slug", "Slug đã tồn tại");
			}
		}
	}

}
