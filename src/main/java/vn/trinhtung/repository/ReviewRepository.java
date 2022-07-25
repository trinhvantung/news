package vn.trinhtung.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Review;
import vn.trinhtung.model.ReviewStarCount;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	Page<Review> findAllByNewsId(Integer newsId, Pageable pageable);

	Review findByNewsIdAndUserId(Integer newsId, Integer userId);

	@Query("SELECT new vn.trinhtung.model.ReviewStarCount(r.star, COUNT(r.star)) FROM Review AS r WHERE r.news.id=:newsId GROUP BY r.star")
	List<ReviewStarCount> countTotalReviewStarByStar(Integer newsId);
}
