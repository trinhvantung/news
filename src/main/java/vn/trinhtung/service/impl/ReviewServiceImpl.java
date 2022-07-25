package vn.trinhtung.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.entity.Review;
import vn.trinhtung.model.ReviewStarCount;
import vn.trinhtung.repository.ReviewRepository;
import vn.trinhtung.security.NewsUserDetails;
import vn.trinhtung.service.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;

	@Override
	public Page<Review> getAllByNewsId(Integer newsId, Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 2, sort);
		return reviewRepository.findAllByNewsId(newsId, pageable);
	}

	@Override
	public Review getByNewsIdAndUserId(Integer newsId, Integer userId) {
		return reviewRepository.findByNewsIdAndUserId(newsId, userId);
	}

	@Transactional
	@Override
	public Review save(Review review) {
		Review result = null;
		NewsUserDetails userDetails = (NewsUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Review savedReview = reviewRepository.findByNewsIdAndUserId(review.getNews().getId(),
				userDetails.getUser().getId());

		if (savedReview == null) {
			result = reviewRepository.save(review);
		}

		return result;
	}

	@Override
	public Map<String, Object> getReviewDetail(Integer newsId) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<ReviewStarCount> reviewStarCounts = reviewRepository.countTotalReviewStarByStar(newsId);
		long totalReview = 0l;
		float starResult = 0f;
		long totalStar = 0l;

		for (ReviewStarCount rsc : reviewStarCounts) {
			totalReview += rsc.getQuantity();
			totalStar += rsc.getStar() * rsc.getQuantity();
		}

		for (byte i = 1; i <= 5; i++) {
			if (!reviewStarCounts.contains(new ReviewStarCount(i))) {
				reviewStarCounts.add(new ReviewStarCount(i, 0l, 0));
			} else {
				ReviewStarCount reviewStarCount = reviewStarCounts
						.get(reviewStarCounts.indexOf(new ReviewStarCount(i)));
				reviewStarCount.setPercent((int) (reviewStarCount.getQuantity() * 100 / totalReview));
			}
		}
		reviewStarCounts.sort((r1, r2) -> r2.getStar().compareTo(r1.getStar()));

		if (totalReview != 0) {
			starResult = totalStar / (1.0f * totalReview);
		}

		result.put("reviewStarCounts", reviewStarCounts);
		result.put("totalReview", totalReview);
		if (starResult == (int) starResult) {
			result.put("starResult", (int) starResult);
		} else {
			result.put("starResult", Float.valueOf(String.format("%.1f", starResult)));
		}

		return result;
	}

}
