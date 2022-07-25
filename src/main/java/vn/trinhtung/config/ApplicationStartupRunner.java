package vn.trinhtung.config;

import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.entity.Role;
import vn.trinhtung.entity.User;
import vn.trinhtung.repository.RoleRepository;
import vn.trinhtung.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ApplicationStartupRunner implements CommandLineRunner {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		if (roleRepository.findByName("ADMIN") == null) {
			roleRepository.save(new Role("ADMIN"));
		}
		if (roleRepository.findByName("USER") == null) {
			roleRepository.save(new Role("USER"));
		}
		if (roleRepository.findByName("MANAGER") == null) {
			roleRepository.save(new Role("MANAGER"));
		}

		if (userRepository.findByEmail("tungvlhy@gmail.com").isEmpty()) {
			User user = new User();
			user.setEmail("tungvlhy@gmail.com");
			user.setFullname("Admin");
			user.setNonLocked(true);
			user.setPassword(passwordEncoder.encode("12345"));
			user.setRoles(Collections.singletonList(roleRepository.findByName("ADMIN")));
			userRepository.save(user);
		}
	}
}
