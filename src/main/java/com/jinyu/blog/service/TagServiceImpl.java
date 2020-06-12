package com.jinyu.blog.service;

import com.jinyu.blog.dao.TagDao;
import com.jinyu.blog.entity.Tag;
import com.jinyu.blog.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/1 16:20
 */
@Service
public class TagServiceImpl implements TagService{
    @Resource
    private TagDao tagDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag saveTag(Tag type) {
        return tagDao.save(type);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag getTag(Long id) {
        return tagDao.getOne(id);
    }

    @Override
    public List<Tag> listTag(String idsStr) {
        if (idsStr == null || "".equals(idsStr)){
            return null;
        }
        String[] split = idsStr.split(",");
        List<Long> ids = new ArrayList<>();
        Arrays.stream(split).map(Long::valueOf).forEach(ids::add);
        return tagDao.findAllById(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag findByName(String name) {
        return tagDao.findByName(name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagDao.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagDao.findAll();
    }

    @Override
    public List<Tag> listTag(List<Long> ids) {
        return tagDao.findAllById(ids);
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        return tagDao.findTop(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size")));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTag(Long id) {
        tagDao.deleteById(id);
    }
}
