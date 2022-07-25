package vn.trinhtung.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import vn.trinhtung.entity.Review;

public interface ReviewService {
	Page<Review> getAllByNewsId(Integer newsId, Integer page);
	
	Review getByNewsIdAndUserId(Integer newsId, Integer userId);
	
	Review save(Review review);
	
	Map<String, Object> getReviewDetail(Integer newsId);
}
