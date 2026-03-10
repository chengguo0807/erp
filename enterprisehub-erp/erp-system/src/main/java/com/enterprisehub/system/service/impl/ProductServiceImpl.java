package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Product;
import com.enterprisehub.system.mapper.ProductMapper;
import com.enterprisehub.system.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    
    @Override
    public PageResult<Product> selectPage(PageQuery pageQuery) {
        Page<Product> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<Product> result = this.page(page);
        return PageResult.build(result);
    }
    
    @Override
    public Product selectById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean insert(Product entity) {
        return this.save(entity);
    }
    
    @Override
    public boolean update(Product entity) {
        return this.updateById(entity);
    }
    
    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
