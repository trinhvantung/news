package vn.trinhtung.controller.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
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
import vn.trinhtung.entity.Role;
import vn.trinhtung.entity.User;
import vn.trinhtung.service.RoleService;
import vn.trinhtung.service.UserService;
import vn.trinhtung.validator.UserValidator;

@PreAuthorize("hasAuthority('ADMIN')")
@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final RoleService roleService;
	private final UserValidator useValidator;

	@GetMapping
	public String getAll(@RequestParam(defaultValue = "1") Integer page, Model model) {
		model.addAttribute("page", userService.getAll(page));
		return "admin/users";
	}

	@GetMapping("/create")
	public String create(Model model) {
		List<Role> roles = roleService.getAll();
		roles.remove(new Role("ADMIN"));
		model.addAttribute("roleList", roles);
		model.addAttribute("user", new User());
		return "admin/user-form";
	}

	@PostMapping("/create")
	public String processCreate(Model model, @Valid @ModelAttribute("user") User userRequest, BindingResult bindingResult) {
		useValidator.validate(userRequest, bindingResult);

		if (bindingResult.hasErrors()) {
			List<Role> roles = roleService.getAll();
			roles.remove(new Role("ADMIN"));
			model.addAttribute("user", userRequest);
			model.addAttribute("roleList", roles);
			return "admin/user-form";
		}
		userService.save(userRequest);
		return "redirect:/admin/users?create-success";
	}

	@GetMapping("/edit/{id}")
	public String edit(Model model, @PathVariable Integer id) {
		User user = userService.getById(id);
		List<Role> roles = roleService.getAll();
		if (user.getRoles().contains(new Role("ADMIN"))) {
			Role roleAdmin = roles.get(roles.indexOf(new Role("ADMIN")));
			roles.clear();
			roles.add(roleAdmin);
		} else {
			roles.remove(new Role("ADMIN"));
		}
		model.addAttribute("user", user);
		model.addAttribute("roleList", roles);
		return "admin/user-form";
	}

	@PostMapping("/edit")
	public String processEdit(@Valid @ModelAttribute("user") User userRequest, BindingResult bindingResult, Model model) {
		useValidator.validate(userRequest, bindingResult);

		if (bindingResult.hasErrors()) {
//			bindingResult.getAllErrors().forEach((error) -> {
//				String fieldName = ((FieldError) error).getField();
//				String errorMessage = error.getDefaultMessage();
//			});

			User user = userService.getById(userRequest.getId());
			List<Role> roles = roleService.getAll();
			if (user.getRoles().contains(new Role("ADMIN"))) {
				Role roleAdmin = roles.get(roles.indexOf(new Role("ADMIN")));
				roles.clear();
				roles.add(roleAdmin);
			} else {
				roles.remove(new Role("ADMIN"));
			}
			model.addAttribute("roleList", roles);
			model.addAttribute("user", userRequest);

			return "admin/user-form";
		}
		userService.save(userRequest);
		return "redirect:/admin/users?edit-success";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		userService.delete(id);
		return "redirect:/admin/users?delete-success";
	}
}
