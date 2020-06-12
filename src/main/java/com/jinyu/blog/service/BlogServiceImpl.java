package com.jinyu.blog.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.jinyu.blog.dao.BlogDao;
import com.jinyu.blog.dao.CommentDao;
import com.jinyu.blog.entity.Blog;
import com.jinyu.blog.entity.Comment;
import com.jinyu.blog.entity.Type;
import com.jinyu.blog.exception.NotFoundException;
import com.jinyu.blog.util.MarkdownUtils;
import com.jinyu.blog.util.MyBeanUtils;
import com.jinyu.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/2 20:22
 */
@Service
public class BlogServiceImpl implements BlogService{
    @Resource
    private BlogDao blogDao;
    @Resource
    private CommentDao commentDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog getBlog(Long id) {
        return blogDao.getOne(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogDao.getOne(id);
        if (blog == null){
            throw new NotFoundException("不存在该博客");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(b.getContent()));
        blogDao.addViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (blog.getTitle() != null && !"".equals(blog.getTitle())){
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null ){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogDao.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String search, Pageable pageable) {
        return blogDao.findByQuery("%" + search + "%", pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        return blogDao.save(blog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog updateBlog(Blog blog) {
        Blog b = blogDao.getOne(blog.getId());
        if (b == null){
            throw new NotFoundException("不存在该博客");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogDao.save(b);
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        return blogDao.findTop(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTime")));
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        List<Blog> someYearBlogs;
        for (String year : blogDao.findGroupYear()) {
            someYearBlogs = blogDao.findByYear(year);
            Collections.reverse(someYearBlogs);
            map.put(year, someYearBlogs);
        }
        return map;
    }

    @Override
    public Long countArchiveBlog() {
        return blogDao.count();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBlog(Long id) {
        commentDao.deleteAllByBlogId(id);
        blogDao.deleteById(id);
    }
}
