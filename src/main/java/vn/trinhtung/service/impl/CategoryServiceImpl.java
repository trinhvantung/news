package vn.trinhtung.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.entity.Category;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.CategoryRepository;
import vn.trinhtung.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Transactional
	@Override
	public void save(Category category) {
		categoryRepository.save(category);
	}

	@Override
	public Page<Category> getAll(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		return categoryRepository.findAll(pageable);
	}

	@Override
	public Category getById(Integer id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Danh mục không tồn tại"));
	}

	@Transactional
	@Override
	public void deleteById(Integer id) {
		try {
			categoryRepository.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException("Danh mục không tồn tại");
		}
	}

	@Override
	public List<Category> getAll() {
		return categoryRepository.findAll();
	}

	@Override
	public List<Category> getAllByEnableTrue() {
		return categoryRepository.findAllByEnableTrue();
	}

	@Override
	public Category getBySlugAndEnableTrue(String slug) {
		return categoryRepository.findBySlugIgnoreCaseAndEnableTrue(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Danh mục không tồn tại"));
	}

	@Override
	public Long count() {
		return categoryRepository.count();
	}

}
