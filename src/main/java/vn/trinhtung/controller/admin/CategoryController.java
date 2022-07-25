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
import vn.trinhtung.entity.Category;
import vn.trinhtung.service.CategoryService;
import vn.trinhtung.validator.CategoryValidator;

@Controller
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryValidator categoryValidator;
	private final CategoryService categoryService;

	@GetMapping
	public String getAll(@RequestParam(defaultValue = "1") Integer page, Model model) {
		model.addAttribute("page", categoryService.getAll(page));
		return "admin/categories";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("category", new Category());
		return "admin/category-form";
	}

	@PostMapping("/create")
	public String processCreate(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult,
			Model model) {
		categoryValidator.validate(category, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("category", category);
			return "admin/category-form";
		}

		categoryService.save(category);
		return "redirect:/admin/category?create-success";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		model.addAttribute("category", categoryService.getById(id));
		return "admin/category-form";
	}

	@PostMapping("/edit")
	public String processEdit(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult,
			Model model) {
		categoryValidator.validate(category, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("category", category);
			return "admin/category-form";
		}

		categoryService.save(category);
		return "redirect:/admin/category?edit-success";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		categoryService.deleteById(id);
		return "redirect:/admin/category?delete-success";
	}
}
