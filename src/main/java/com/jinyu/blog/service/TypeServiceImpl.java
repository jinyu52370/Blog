package com.jinyu.blog.service;

import com.jinyu.blog.dao.TypeDao;
import com.jinyu.blog.entity.Type;
import com.jinyu.blog.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/1 16:20
 */
@Service
public class TypeServiceImpl implements TypeService{
    @Resource
    private TypeDao typeDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type saveType(Type type) {
        return typeDao.save(type);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type getType(Long id) {
        return typeDao.getOne(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type findByName(String name) {
        return typeDao.findByName(name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeDao.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeDao.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        return typeDao.findTop(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size")));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteType(Long id) {
        typeDao.deleteById(id);
    }
}
