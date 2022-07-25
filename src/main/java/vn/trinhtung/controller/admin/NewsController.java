package vn.trinhtung.controller.admin;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.entity.News;
import vn.trinhtung.service.CategoryService;
import vn.trinhtung.service.NewsService;
import vn.trinhtung.validator.NewsValidator;

@Controller("admin-news")
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;
	private final CategoryService categoryService;
	private final NewsValidator newsValidator;

	@GetMapping
	public String getAll(@RequestParam(defaultValue = "1") Integer page, Model model) {
		model.addAttribute("page", newsService.getAll(page));
		return "admin/posts";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("news", new News());
		model.addAttribute("categories", categoryService.getAll());
		return "admin/post-form";
	}

	@PostMapping("/create")
	public String prcessCreate(Model model, @Valid @ModelAttribute("news") News news, BindingResult bindingResult) {
		newsValidator.validate(news, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("news", news);
			model.addAttribute("categories", categoryService.getAll());
			return "admin/post-form";
		}
		newsService.save(news);
		return "redirect:/admin/news?create-success";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		model.addAttribute("news", newsService.getById(id));
		model.addAttribute("categories", categoryService.getAll());
		return "admin/post-form";
	}

	@PostMapping("/edit")
	public String processEdit(Model model, @Valid @ModelAttribute("news") News news, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("news", news);
			model.addAttribute("categories", categoryService.getAll());
			return "admin/post-form";
		}
		newsService.save(news);
		return "redirect:/admin/news?edit-success";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		newsService.deleteById(id);
		return "redirect:/admin/news?delete-success";
	}
}
