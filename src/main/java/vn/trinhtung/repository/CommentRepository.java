package vn.trinhtung.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByNewsId(Integer newsId, Integer offset, Integer limit);
	
	List<Comment> findAllByNewsIdAndParentid(Integer newsId, Long parentId, Integer offset, Integer limit);
	
	Long countByNewsIdAndParentIsNull(Integer newsId);
}