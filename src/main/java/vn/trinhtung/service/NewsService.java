package vn.trinhtung.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.SearchRequest;
import vn.trinhtung.entity.News;

public interface NewsService {
	Page<News> getAll(Integer page);

	News getById(Integer id);
	
	void save(News news);
	
	News getBySlug(String slug);
	
	Page<News> getAllByCategorySlug(Integer page, String categorySlug);
	
	List<News> getFirst13ByEnableTrueAndCategoryEnableTrue();
	
	News getBySlugAndEnableTrueAndCategoryEnableTrue(String slug);
	
	Page<News> search(SearchRequest searchRequest);
	
	void index();
	
	Long count();
	
	void deleteById(Integer id);
	
	Map<String, Long> countNewsGroupByCategory();
	
	Map<String, Long> countNewsGroupByMonth(String startDate, String endDate);
}
