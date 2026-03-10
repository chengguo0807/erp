package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Warehouse;
import com.enterprisehub.system.mapper.WarehouseMapper;
import com.enterprisehub.system.service.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Override
    public PageResult<Warehouse> selectPage(PageQuery pageQuery) {
        Page<Warehouse> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<Warehouse> result = this.page(page);
        return PageResult.build(result);
    }

    @Override
    public Warehouse selectById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean insert(Warehouse entity) {
        return this.save(entity);
    }

    @Override
    public boolean update(Warehouse entity) {
        return this.updateById(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
