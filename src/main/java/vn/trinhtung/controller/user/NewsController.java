package vn.trinhtung.controller.user;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.CommentRequest;
import vn.trinhtung.dto.SearchRequest;
import vn.trinhtung.entity.Comment;
import vn.trinhtung.entity.News;
import vn.trinhtung.entity.Review;
import vn.trinhtung.entity.User;
import vn.trinhtung.security.NewsUserDetails;
import vn.trinhtung.service.CategoryService;
import vn.trinhtung.service.CommentService;
import vn.trinhtung.service.NewsService;
import vn.trinhtung.service.ReviewService;

@Controller("news-user")
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;
	private final CategoryService categoryService;
	private final ReviewService reviewService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final CommentService commentService;

	@GetMapping("/danh-muc/{slug}")
	public String getByCategorySlug(@PathVariable String slug, Model model,
			@RequestParam(defaultValue = "1") Integer page) {
		model.addAttribute("page", newsService.getAllByCategorySlug(page, slug));
		model.addAttribute("category", categoryService.getBySlugAndEnableTrue(slug));
		return "user/post-list";
	}

	@GetMapping("/bai-viet/{slug}")
	public String getBySlug(@PathVariable String slug, Model model,
			@AuthenticationPrincipal NewsUserDetails userDetails,
			@RequestParam(defaultValue = "1") Integer pageReview) {
		News news = newsService.getBySlugAndEnableTrueAndCategoryEnableTrue(slug);
		Review review = new Review();
		review.setNews(new News(news.getId()));
		review.setStar((byte) 5);

		model.addAttribute("news", news);
		model.addAttribute("review", review);
		model.addAttribute("pageReview", reviewService.getAllByNewsId(news.getId(), pageReview));
		model.addAttribute("reviewDetail", reviewService.getReviewDetail(news.getId()));

		if (userDetails != null) {
			model.addAttribute("myReview",
					reviewService.getByNewsIdAndUserId(news.getId(), userDetails.getUser().getId()));
			model.addAttribute("userReview",
					reviewService.getByNewsIdAndUserId(news.getId(), userDetails.getUser().getId()));
		}
		return "user/post-detail";
	}

	@ResponseBody
	@PostMapping("/bai-viet/{id}/danh-gia")
	public Review processReview(@PathVariable Integer id, @Valid @RequestBody Review review,
			@AuthenticationPrincipal NewsUserDetails userDetails) {
		User user = new User();
		user.setId(userDetails.getUser().getId());
		user.setFullname(userDetails.getUser().getFullname());
		user.setImage(userDetails.getUser().getImage());

		review.setUser(user);
		review.setNews(new News(id));
		review.setId(null);

		Review result = reviewService.save(review);
		return result;
	}

	@MessageMapping("/comment/{newsId}")
	public void processComment(@DestinationVariable Integer newsId, @Payload CommentRequest comment,
			Principal principal) {
		User user = ((NewsUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUser();
		Comment result = commentService.save(comment, user);
		simpMessagingTemplate.convertAndSend("/topic/news/" + newsId, result);
	}

	@MessageMapping("/comments/{newsId}/{offset}")
	public void getComments(@DestinationVariable Integer newsId, @DestinationVariable Integer offset,
			@Payload(required = false) Long parentId, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
		String sessionId = simpMessageHeaderAccessor.getSessionId();
		if (parentId == null) {
			simpMessagingTemplate.convertAndSend("/topic/news/" + newsId + "/comments/" + sessionId,
					commentService.getAllByNewsId(newsId, offset, 3));
		} else {
			simpMessagingTemplate.convertAndSend("/topic/news/" + newsId + "/comments/" + sessionId,
					commentService.getAllByNewsIdAndParendId(newsId, parentId, offset, 3));
		}
	}

	@GetMapping("/tim-kiem")
	public String search(@ModelAttribute SearchRequest searchRequest, Model model) {
		model.addAttribute("page", newsService.search(searchRequest));
		model.addAttribute("searchRequest", searchRequest);
		return "user/search-result";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/index-data")
	public String index() {
		newsService.index();
		return "redirect:/";
	}
}
