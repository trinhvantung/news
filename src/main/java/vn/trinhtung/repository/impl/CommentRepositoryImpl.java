package vn.trinhtung.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vn.trinhtung.entity.Comment;
import vn.trinhtung.entity.User;

public class CommentRepositoryImpl {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Comment> findAllByNewsId(Integer newsId, Integer offset, Integer limit) {
		String sql = "select c.id, c.content, c.createdDate, u.fullname, u.image,"
				+ " (select count(r) from Comment r where r.parent.id = c.id) as countReply "
				+ "from Comment c inner join c.user u where c.parent.id is null "
				+ "and c.news.id = :newsId order by c.id desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter("newsId", newsId);
		query.setFirstResult(offset);
		query.setMaxResults(limit);

		List<Object[]> queryResult = query.getResultList();
		List<Comment> comments = new ArrayList<Comment>();

		queryResult.forEach(objects -> {
			Comment comment = new Comment();
			User user = new User();
			user.setFullname((String) objects[3]);
			user.setImage((String) objects[4]);

			comment.setId((Long) objects[0]);
			comment.setContent((String) objects[1]);
			comment.setUser(user);
			comment.setCreatedDate((Date) objects[2]);
			comment.setCountReply((Long) objects[5]);
			comments.add(comment);
		});

		return comments;
	}

	@SuppressWarnings("unchecked")
	public List<Comment> findAllByNewsIdAndParentid(Integer newsId, Long parentId, Integer offset, Integer limit) {
		String sql = "select c.id, c.content, c.parent.id, c.createdDate, u.fullname, u.image "
				+ "from Comment c inner join c.user u where c.parent.id = :parentId "
				+ "and c.news.id = :newsId order by c.id desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter("parentId", parentId);
		query.setParameter("newsId", newsId);
		query.setFirstResult(offset);
		query.setMaxResults(limit);

		List<Object[]> queryResult = query.getResultList();
		List<Comment> comments = new ArrayList<Comment>();

		queryResult.forEach(objects -> {
			Comment comment = new Comment();
			User user = new User();

			comment.setId((Long) objects[0]);
			comment.setContent((String) objects[1]);
			comment.setParent(new Comment((Long) objects[2]));
			comment.setCreatedDate((Date) objects[3]);

			user.setFullname((String) objects[4]);
			user.setImage((String) objects[5]);
			comment.setUser(user);
			comments.add(comment);
		});

		return comments;
	}
}
