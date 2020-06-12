package com.jinyu.blog.service;

import com.jinyu.blog.entity.Comment;

import java.util.List;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/6 17:49
 */
public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment save(Comment comment);
}
