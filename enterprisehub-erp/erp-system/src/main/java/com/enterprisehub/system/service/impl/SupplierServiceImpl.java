package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Supplier;
import com.enterprisehub.system.mapper.SupplierMapper;
import com.enterprisehub.system.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 供应商服务实现类
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Override
    public PageResult<Supplier> selectSupplierList(PageQuery pageQuery, String supplierName) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(supplierName), Supplier::getSupplierName, supplierName)
               .orderByDesc(Supplier::getCreateTime);
        
        Page<Supplier> page = this.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), wrapper);
        return PageResult.build(page);
    }

    @Override
    public Supplier selectSupplierById(Long id) {
        return this.getById(id);
    }

    @Override
    public int insertSupplier(Supplier supplier) {
        return this.save(supplier) ? 1 : 0;
    }

    @Override
    public int updateSupplier(Supplier supplier) {
        return this.updateById(supplier) ? 1 : 0;
    }

    @Override
    public int deleteSupplierById(Long id) {
        return this.removeById(id) ? 1 : 0;
    }
}
