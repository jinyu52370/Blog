package com.jinyu.blog.service;

import com.jinyu.blog.entity.Blog;
import com.jinyu.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String search, Pageable pageable);

    Blog saveBlog(Blog blog);

    void deleteBlog(Long id);

    Blog updateBlog(Blog blog);

    List<Blog> listBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Long countArchiveBlog();
}
