package com.jinyu.blog.dao;

import com.jinyu.blog.entity.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeDao extends JpaRepository<Type, Long> {
    Type findByName(String name);

    @Query("SELECT t FROM Type t")
    List<Type> findTop(Pageable pageable);
}
