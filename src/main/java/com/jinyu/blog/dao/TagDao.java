package com.jinyu.blog.dao;

import com.jinyu.blog.entity.Tag;
import com.jinyu.blog.entity.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagDao extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    @Query("SELECT t FROM Tag t")
    List<Tag> findTop(Pageable pageable);
}
