package vn.trinhtung.service;

import java.util.List;
import java.util.Map;

import vn.trinhtung.dto.CommentRequest;
import vn.trinhtung.entity.Comment;
import vn.trinhtung.entity.User;

public interface CommentService {
	Comment save(CommentRequest comment, User user);
	
	List<Comment> getAll(Integer newsId, Integer offset);
	
	Map<String, Object> getAllByNewsId(Integer newsId, Integer offset, Integer limit);
	
	Map<String, Object> getAllByNewsIdAndParendId(Integer newsId, Long parentId, Integer offset, Integer limit);
}
