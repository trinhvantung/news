package vn.trinhtung.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.UserRequest;
import vn.trinhtung.entity.User;
import vn.trinhtung.exception.NotFoundException;

public interface UserService {
	void register(UserRequest user);
	
	Page<User> getAll(Integer page);
	
	void save(User user);
	
	User getById(Integer id);
	
	User update(UserRequest user);
	
	void updatePassword(UserRequest user);
	
	Long count();
	
	void delete(Integer id);
	
	void forgotPassword(String email, HttpServletRequest request) throws NotFoundException;
	
	boolean activePassword(String token);
}
