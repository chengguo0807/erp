package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.ProductCategory;
import com.enterprisehub.system.mapper.ProductCategoryMapper;
import com.enterprisehub.system.service.ProductCategoryService;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Override
    public PageResult<ProductCategory> selectPage(PageQuery pageQuery) {
        Page<ProductCategory> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<ProductCategory> result = this.page(page);
        return PageResult.build(result);
    }

    @Override
    public ProductCategory selectById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean insert(ProductCategory entity) {
        return this.save(entity);
    }

    @Override
    public boolean update(ProductCategory entity) {
        return this.updateById(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
