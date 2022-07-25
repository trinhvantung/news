package vn.trinhtung.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.trinhtung.entity.Category;

public interface CategoryService {
	void save(Category category);

	Page<Category> getAll(Integer page);

	Category getById(Integer id);

	void deleteById(Integer id);

	List<Category> getAll();
	
	List<Category> getAllByEnableTrue();
	
	Category getBySlugAndEnableTrue(String slug);
	
	Long count();
}
