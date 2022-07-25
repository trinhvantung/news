package vn.trinhtung.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.UserRequest;
import vn.trinhtung.entity.User;
import vn.trinhtung.exception.NotFoundException;
import vn.trinhtung.security.NewsUserDetails;
import vn.trinhtung.service.UserService;
import vn.trinhtung.validator.UserRequestValidator;

@Controller
@RequiredArgsConstructor
public class AccountController {
	private final UserService userService;
	private final UserRequestValidator userRequestValidator;

	@GetMapping("/dang-nhap")
	public String login() {
		return "user/login";
	}

	@GetMapping("/dang-ky")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "user/register";
	}

	@PostMapping("/dang-ky")
	public String handleRegister(@Valid @ModelAttribute("user") UserRequest user, BindingResult bindingResult,
			Model model) {
		user.setId(null);

		userRequestValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "user/register";
		}
		userService.register(user);

		return "redirect:/";
	}

	@GetMapping("/quen-mat-khau")
	public String forgotPassword() {
		return "user/forgot-password";
	}

	@PostMapping("/quen-mat-khau")
	public String processForgotPassword(@RequestParam String email, HttpServletRequest request, Model model) {
		try {
			userService.forgotPassword(email, request);
		} catch (NotFoundException e) {
			model.addAttribute("email", email);
			model.addAttribute("message", e.getMessage());
			return "user/forgot-password";
		}
		return "redirect:/quen-mat-khau?email=" + email + "&success";
	}

	@GetMapping("/quen-mat-khau/{token}")
	public String activePassword(@PathVariable String token) {
		if (!userService.activePassword(token)) {
			return "redirect:/quen-mat-khau?active-fail";
		}
		return "redirect:/quen-mat-khau?active-success";
	}

	@GetMapping("/ho-so")
	public String profile(Model model, @AuthenticationPrincipal NewsUserDetails userDetails) {
		model.addAttribute("user", userService.getById(userDetails.getUser().getId()));
		return "user/profile";
	}

	@PostMapping("/ho-so")
	public String processUpdateProfile(@Valid @ModelAttribute UserRequest user,
			@AuthenticationPrincipal NewsUserDetails userDetails, BindingResult bindingResult, Model model) {
		user.setId(userDetails.getUser().getId());

		userRequestValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "user/profile";
		}
		NewsUserDetails details = new NewsUserDetails(userService.update(user));
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details, null,
				details.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(token);
		return "redirect:/ho-so?success";
	}

	@ResponseBody
	@PostMapping("/doi-mat-khau")
	public ResponseEntity<?> processUpdatePassword(@Valid @RequestBody UserRequest user,
			@AuthenticationPrincipal NewsUserDetails userDetails, BindingResult bindingResult, Model model) {
		user.setId(userDetails.getUser().getId());

		userRequestValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getAllErrors().forEach((error) -> {
				String fieldName = ((FieldError) error).getField();
				String errorMessage = error.getDefaultMessage();
				errors.put(fieldName, errorMessage);
			});
			return ResponseEntity.badRequest().body(errors);
		}
		userService.updatePassword(user);
		return ResponseEntity.ok().build();
	}
}
