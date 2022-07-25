package vn.trinhtung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByNameIgnoreCase(String name);

	Category findBySlugIgnoreCase(String slug);

	List<Category> findAllByEnableTrue();

	Optional<Category> findBySlugIgnoreCaseAndEnableTrue(String slug);
}
