package vn.trinhtung.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	@Value("${email.config.host}")
	private String host;

	@Value("${email.config.username}")
	private String username;

	@Value("${email.config.password}")
	private String password;

	@Value("${email.config.port}")
	private Integer port;

	@Value("${email.config.auth}")
	private String auth;

	@Value("${email.config.enable}")
	private String enable;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		Properties mailProperties = mailSender.getJavaMailProperties();
		mailProperties.setProperty("mail.smtp.auth", auth);
		mailProperties.setProperty("mail.smtp.starttls.enable", enable);

		return mailSender;
	}
}
