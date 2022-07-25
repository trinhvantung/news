package vn.trinhtung.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import vn.trinhtung.service.CategoryService;

@Component
public class CategoryInterceptor implements HandlerInterceptor {
	@Autowired
	private CategoryService categoryService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("INTERCEPTOR");
		request.setAttribute("categories", categoryService.getAllByEnableTrue());
	}
}
