package vn.trinhtung.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.service.CloudinaryService;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
	private final Cloudinary cloudinary;

	@Override
	public Map<?, ?> upload(MultipartFile multipartFile, String folder) {
		Map<?, ?> result = null;
		try {
			Map<String, String> option = new HashMap<>();
			option.put("resource_type", "auto");
			option.put("folder", folder);

			result = cloudinary.uploader().upload(multipartFile.getBytes(), option);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String upload(String folder, MultipartFile multipartFile) {
		Map<?, ?> result = null;
		try {
			Map<String, String> option = new HashMap<>();
			option.put("resource_type", "auto");
			option.put("folder", folder);

			result = cloudinary.uploader().upload(multipartFile.getBytes(), option);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) result.get("public_id");
	}

	@Override
	public void delete(String publicId) {
		try {
			cloudinary.uploader().destroy(publicId, Collections.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(List<String> publicIds) {
		try {
			cloudinary.api().deleteResources(publicIds, Collections.emptyMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
