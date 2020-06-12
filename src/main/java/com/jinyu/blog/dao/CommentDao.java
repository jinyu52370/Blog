package com.jinyu.blog.dao;

import com.jinyu.blog.entity.Blog;
import com.jinyu.blog.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogId(Long blogId, Sort sort);

    void deleteAllByBlogId(Long blogId);
}
