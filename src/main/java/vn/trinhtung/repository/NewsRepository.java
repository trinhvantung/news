package vn.trinhtung.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
	News findByTitleIgnoreCase(String title);

	News findBySlugIgnoreCase(String slug);

	@Query("SELECT n FROM News n WHERE n.category.slug=:slug AND n.enable=true")
	Page<News> findAllByCategorySlugAndEnableTrue(String slug, Pageable pageable);

	List<News> findFirst13ByEnableTrueAndCategoryEnableTrueOrderByIdDesc();

	Optional<News> findBySlugAndEnableTrueAndCategoryEnableTrue(String slug);

	@Query("SELECT n.category.name, COUNT(n) FROM News n GROUP BY n.category.name")
	List<Object[]> countNewsGroupByCategory();

	@Query("SELECT function('date_trunc', 'month', n.createdDate), count(n) FROM News n "
			+ "WHERE n.createdDate BETWEEN :startDate AND :endDate "
			+ "GROUP BY function('date_trunc', 'month', n.createdDate)")
	public List<Object[]> countNewsGroupByMonth(Date startDate, Date endDate);

}
