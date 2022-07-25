package vn.trinhtung.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import vn.trinhtung.dto.UserRequest;
import vn.trinhtung.entity.PasswordReset;
import vn.trinhtung.entity.Role;
import vn.trinhtung.entity.User;
import vn.trinhtung.exception.NotFoundException;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.PasswordResetRepository;
import vn.trinhtung.repository.RoleRepository;
import vn.trinhtung.repository.UserRepository;
import vn.trinhtung.service.CloudinaryService;
import vn.trinhtung.service.EmailService;
import vn.trinhtung.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authenticationManager;
	private final CloudinaryService cloudinaryService;
	private final EmailService emailService;
	private final PasswordResetRepository passwordResetRepository;

	@Override
	public void register(UserRequest userRegistration) {
		User user = new User();

		user.setEmail(userRegistration.getEmail());
		user.setFullname(userRegistration.getFullname());
		user.setNonLocked(true);
		user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
		user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));

		userRepository.save(user);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userRegistration.getEmail(),
				userRegistration.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Override
	public Page<User> getAll(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		return userRepository.findAll(pageable);
	}

	@Transactional
	@Override
	public void save(User userRequest) {

		User user = new User();
		user.setId(userRequest.getId());
		user.setEmail(userRequest.getEmail());
		user.setFullname(userRequest.getFullname());
		user.setNonLocked(userRequest.isNonLocked());
		user.setRoles(userRequest.getRoles().stream().map(role -> new Role(role.getId())).collect(Collectors.toList()));

		if (userRequest.getId() == null) {
			user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
			if (userRequest.getRoles().contains(roleRepository.findByName("ADMIN"))) {
				return;
			}
		} else {
			User savedUser = userRepository.findById(userRequest.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

			user.setImage(savedUser.getImage());

			if (!userRequest.getPassword().isEmpty()) {
				user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
			} else {
				user.setPassword(savedUser.getPassword());
			}

			if (savedUser.getRoles().contains(new Role("ADMIN"))) {
				user.setRoles(savedUser.getRoles());
				user.setNonLocked(true);
			}
		}

		userRepository.save(user);
	}

	@Override
	public User getById(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
	}

	@Transactional
	@Override
	public User update(UserRequest user) {
		User savedUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
		savedUser.setFullname(user.getFullname());

		if (user.getImageFile() != null && !user.getImageFile().getOriginalFilename().isEmpty()) {
			if (savedUser.getImage() != null) {
				cloudinaryService.delete(savedUser.getImage());
			}
			String publicId = cloudinaryService.upload("user", user.getImageFile());
			savedUser.setImage(publicId);
		}
		return userRepository.save(savedUser);
	}

	@Override
	public void updatePassword(UserRequest user) {
		User savedUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
		savedUser.setPassword(passwordEncoder.encode(user.getNewPassword()));

		userRepository.save(savedUser);
	}

	@Override
	public Long count() {
		return userRepository.count();
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
		if (user.getImage() != null && !user.getImage().isEmpty()) {
			cloudinaryService.delete(user.getImage());
		}
		userRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void forgotPassword(String email, HttpServletRequest request) throws NotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email không tồn tại"));

		String token = RandomString.make(64);
		String requestUrl = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String urlVerify = requestUrl.substring(0, requestUrl.indexOf(servletPath)) + "/quen-mat-khau/" + token;
		String newPassword = RandomString.make(6).toLowerCase();
		String subject = "Mật khẩu mới";

		Date expire = new Date(System.currentTimeMillis() + (15 * 60 * 1000));
		PasswordReset passwordReset = new PasswordReset(null, token, newPassword, expire, user);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("newPassword", newPassword);
		attributes.put("urlVerify", urlVerify);

		emailService.sendMessageHtml(email, subject, "reset-password", attributes);
		passwordResetRepository.save(passwordReset);
	}

	@Transactional
	@Override
	public boolean activePassword(String token) {
		PasswordReset passwordReset = passwordResetRepository.findByToken(token).orElse(null);
		if (passwordReset == null || passwordReset.getExpire().before(new Date())) {
			return false;
		}
		User user = passwordReset.getUser();
		user.setPassword(passwordEncoder.encode(passwordReset.getPassword()));
		userRepository.save(user);
		passwordResetRepository.deleteById(passwordReset.getId());
		return true;
	}

}
