package vn.trinhtung.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.service.NewsService;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final NewsService newsService;

	@GetMapping
	public String home(Model model) {
		model.addAttribute("newsList", newsService.getFirst13ByEnableTrueAndCategoryEnableTrue());
		return "user/home";
	}
}
