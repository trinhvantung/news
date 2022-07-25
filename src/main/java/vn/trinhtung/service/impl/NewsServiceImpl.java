package vn.trinhtung.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.SearchRequest;
import vn.trinhtung.entity.News;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.NewsRepository;
import vn.trinhtung.service.CloudinaryService;
import vn.trinhtung.service.NewsService;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
	private final NewsRepository newsRepository;
	private final CloudinaryService cloudinaryService;
	private final EntityManager entityManager;

	@Override
	public Page<News> getAll(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return newsRepository.findAll(pageable);
	}

	@Override
	public News getById(Integer id) {
		return newsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));
	}

	@Override
	public void save(News news) {
		if (news.getImageFile() != null && !news.getImageFile().getOriginalFilename().isEmpty()) {
			if (news.getId() != null) {
				News savedNews = newsRepository.findById(news.getId()).get();
				cloudinaryService.delete(savedNews.getImage());
			}
			String publicId = cloudinaryService.upload("news", news.getImageFile());
			news.setImage(publicId);
		}

		if (news.getId() != null) {
			if (news.getImageFile() == null || news.getImageFile().getOriginalFilename().isEmpty()) {
				News savedNews = newsRepository.findById(news.getId()).get();
				news.setImage(savedNews.getImage());
			}
		}
		newsRepository.save(news);
	}

	@Override
	public News getBySlug(String slug) {
		return newsRepository.findBySlugIgnoreCase(slug);
	}

	@Override
	public Page<News> getAllByCategorySlug(Integer page, String categorySlug) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		return newsRepository.findAllByCategorySlugAndEnableTrue(categorySlug, pageable);
	}

	@Override
	public List<News> getFirst13ByEnableTrueAndCategoryEnableTrue() {
		return newsRepository.findFirst13ByEnableTrueAndCategoryEnableTrueOrderByIdDesc();
	}

	@Override
	public News getBySlugAndEnableTrueAndCategoryEnableTrue(String slug) {
		return newsRepository.findBySlugAndEnableTrueAndCategoryEnableTrue(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));
	}

	@Override
	public Page<News> search(SearchRequest searchRequest) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(News.class)
				.get();

		MustJunction junction = queryBuilder.bool()
				.must(queryBuilder.keyword().fuzzy().withPrefixLength(1).onFields("title", "description", "content")
						.boostedTo(5).matching(searchRequest.getQuery()).createQuery());

		if (searchRequest.getCategory() != null && !searchRequest.getCategory().trim().isEmpty()) {
			junction = junction.must(queryBuilder.keyword().onField("category.slug")
					.matching(searchRequest.getCategory().trim()).createQuery());
		}

		Query query = junction.createQuery();

		FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, News.class);

		Integer limit = 10;
		Integer offset = (searchRequest.getPage() - 1) * limit;

		fullTextQuery.setMaxResults(10);
		fullTextQuery.setFirstResult(offset);

		Pageable pageable = PageRequest.of(searchRequest.getPage() - 1, limit);

		@SuppressWarnings("unchecked")
		Page<News> page = new PageImpl<News>(fullTextQuery.getResultList(), pageable, fullTextQuery.getResultSize());

		return page;
	}

	@Override
	public void index() {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Long count() {
		return newsRepository.count();
	}

	@Transactional
	@Override
	public void deleteById(Integer id) {
		News news = newsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));
		cloudinaryService.delete(news.getImage());
		newsRepository.deleteById(id);
	}

	@Override
	public Map<String, Long> countNewsGroupByCategory() {
		List<Object[]> count = newsRepository.countNewsGroupByCategory();
		Map<String, Long> result = new HashMap<String, Long>();

		for (Object[] c : count) {
			System.out.println((String) c[0] + "      " + (Long) c[1]);
			result.put((String) c[0], (Long) c[1]);
		}
		return result;
	}

	@Override
	public Map<String, Long> countNewsGroupByMonth(String startDate, String endDate) {
		Date start = null;
		Date end = null;
		if (startDate == null || endDate == null) {
			end = new Date();

			Date temp = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			c.add(Calendar.MONTH, -3);
			start = c.getTime();

			System.out.println(start);
			System.out.println(end);
		} else {
			try {
				SimpleDateFormat formatStart = new SimpleDateFormat("MM-yyyy");
				start = formatStart.parse(startDate);

				SimpleDateFormat formatEnd = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
				Date temp = formatStart.parse(endDate);
				Calendar c = Calendar.getInstance();
				c.setTime(temp);
				int maxDateOfMonth = c.getActualMaximum(Calendar.DATE);

				end = formatEnd.parse(maxDateOfMonth + "-" + endDate + " 23:59:59");
				System.out.println(start);
				System.out.println(end);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		List<Object[]> count = newsRepository.countNewsGroupByMonth(start, end);
		Map<String, Long> result = new TreeMap<String, Long>();
		SimpleDateFormat format = new SimpleDateFormat("'Tháng 'MM-yyyy");
		for (Object[] c : count) {
			System.out.println(format.format(c[0]) + "      " + (Long) c[1]);
			result.put(format.format(c[0]), (Long) c[1]);
		}
		return result;
	}

}
