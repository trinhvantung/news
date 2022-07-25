package vn.trinhtung.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.interceptor.CategoryInterceptor;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class WebConfig implements WebMvcConfigurer {
	private final CategoryInterceptor categoryInterceptor;
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.addBasenames("classpath:messages");
		return messageSource;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		addResource(registry, "image-user");
		addResource(registry, "image-news");
	}

	private void addResource(ResourceHandlerRegistry registry, String s) {
		Path path = Paths.get(s);
		String url = path.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + s + "/**").addResourceLocations("file:/" + url + "/");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(categoryInterceptor).excludePathPatterns("/admin/**", "/user/**");
	}
}
