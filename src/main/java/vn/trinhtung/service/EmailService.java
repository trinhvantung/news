package vn.trinhtung.service;

import java.util.Map;

public interface EmailService {
	void sendMessageHtml(String to, String subject, String template, Map<String, Object> attributes);
}
