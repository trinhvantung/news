package vn.trinhtung.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
	Map<?, ?> upload(MultipartFile multipartFile, String folder);

	String upload(String folder, MultipartFile multipartFile);

	void delete(String publicId);

	void delete(List<String> publicIds);
}
