package vn.trinhtung.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.CommentRequest;
import vn.trinhtung.entity.Comment;
import vn.trinhtung.entity.News;
import vn.trinhtung.entity.User;
import vn.trinhtung.repository.CommentRepository;
import vn.trinhtung.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;

	@Transactional
	@Override
	public Comment save(CommentRequest comment, User user) {
		Comment c = new Comment();
		User u = new User();

		u.setId(user.getId());
		u.setFullname(user.getFullname());
		u.setImage(user.getImage());
		c.setContent(comment.getContent());

		if (comment.getParentId() != null) {
			c.setParent(new Comment(comment.getParentId()));
		}

		c.setNews(new News(comment.getNewsId()));
		c.setUser(u);

		return commentRepository.save(c);
	}

	@Override
	public List<Comment> getAll(Integer newsId, Integer offset) {
//		Sort sort = Sort.by("id").descending();
//		Pageable pageable = PageRequest.of(0, 0, sort);
		return null;
	}

	@Override
	public Map<String, Object> getAllByNewsId(Integer newsId, Integer offset, Integer limit) {
		Long count = commentRepository.countByNewsIdAndParentIsNull(newsId);
		boolean isLastPage = false;
		if (count - offset <= limit) {
			isLastPage = true;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isLastPage", isLastPage);
		result.put("comments", commentRepository.findAllByNewsId(newsId, offset, limit));
//		return commentRepository.findAllByNewsId(newsId, offset, limit);

		return result;
	}

	@Override
	public Map<String, Object> getAllByNewsIdAndParendId(Integer newsId, Long parentId, Integer offset, Integer limit) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("comments", commentRepository.findAllByNewsIdAndParentid(newsId, parentId, offset, limit));
//		return commentRepository.findAllByNewsIdAndParentid(newsId, parentId, offset, limit);

		return result;
	}

}
