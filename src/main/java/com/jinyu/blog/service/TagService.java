package com.jinyu.blog.service;

import com.jinyu.blog.entity.Tag;
import com.jinyu.blog.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag type);

    Tag getTag(Long id);

    List<Tag> listTag(String ids);

    Tag findByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTag(List<Long> ids);

    List<Tag> listTagTop(Integer size);

    void deleteTag(Long id);
}
