package vn.trinhtung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.trinhtung.security.NewsAuthenticationSuccessHandler;
import vn.trinhtung.security.NewsUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private NewsUserDetailsService userDetailsService;
	@Autowired
	private NewsAuthenticationSuccessHandler authenticationSuccessHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/dang-nhap", "/dang-ky", "/quen-mat-khau", "/user/**")
				.permitAll().antMatchers("/admin/**").hasAnyAuthority("ADMIN", "MANAGER").anyRequest().permitAll();

		http.formLogin().loginPage("/dang-nhap").loginProcessingUrl("/dang-nhap").usernameParameter("email")
				.passwordParameter("password").successHandler(authenticationSuccessHandler).and().rememberMe()
				.rememberMeParameter("remember-me").tokenValiditySeconds(24 * 60 * 60).and().logout()
				.logoutUrl("/dang-xuat").logoutSuccessUrl("/");
	}
}
