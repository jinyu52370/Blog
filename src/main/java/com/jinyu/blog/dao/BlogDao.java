package com.jinyu.blog.dao;

import com.jinyu.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogDao extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    @Query("SELECT b FROM Blog b WHERE b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.title like ?1 OR b.content like ?1")
    Page<Blog> findByQuery(String search, Pageable pageable);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("UPDATE Blog b SET b.views = b.views + 1 WHERE b.id = ?1")
    Integer addViews(Long id);

    @Query("SELECT " +
            "FUNCTION('date_format',b.updateTime,'%Y') AS yser " +
            "FROM Blog b " +
            "GROUP BY FUNCTION('date_format',b.updateTime,'%Y') " +
            "ORDER BY FUNCTION('date_format',b.updateTime,'%Y') DESC")
    List<String> findGroupYear();

    @Query("SELECT b " +
            "FROM Blog b " +
            "WHERE FUNCTION('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("UPDATE Blog b SET b.picture = ?2 WHERE b.id = ?1")
    Integer replaceFastDfs(Long id, String picture);
}
