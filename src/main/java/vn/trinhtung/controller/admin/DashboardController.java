package vn.trinhtung.controller.admin;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.service.CategoryService;
import vn.trinhtung.service.NewsService;
import vn.trinhtung.service.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashboardController {
	private final UserService userService;
	private final NewsService newsService;
	private final CategoryService categoryService;

	@GetMapping
	public String dashboard(Model model) {
		model.addAttribute("news", newsService.count());
		model.addAttribute("category", categoryService.count());
		model.addAttribute("user", userService.count());
		model.addAttribute("newsByCategory", newsService.countNewsGroupByCategory());
		model.addAttribute("newsByMonth", newsService.countNewsGroupByMonth(null, null));
		return "admin/dashboard";
	}

	@PostMapping("/news-by-month")
	@ResponseBody
	public Map<String, Long> newsByMonth(@RequestParam String startDate, @RequestParam String endDate) {
		System.out.println(startDate + "  " + endDate);
		return newsService.countNewsGroupByMonth(startDate, endDate);
	}
}
