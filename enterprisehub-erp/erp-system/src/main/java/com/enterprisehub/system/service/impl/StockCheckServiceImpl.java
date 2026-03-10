package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.StockCheck;
import com.enterprisehub.system.mapper.StockCheckMapper;
import com.enterprisehub.system.service.StockCheckService;
import org.springframework.stereotype.Service;

/**
 * 库存盘点服务实现类
 */
@Service
public class StockCheckServiceImpl extends ServiceImpl<StockCheckMapper, StockCheck> implements StockCheckService {

    @Override
    public PageResult<StockCheck> selectPage(PageQuery pageQuery) {
        Page<StockCheck> page = this.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
        return PageResult.build(page);
    }

    @Override
    public StockCheck selectById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean insert(StockCheck entity) {
        return this.save(entity);
    }

    @Override
    public boolean update(StockCheck entity) {
        return this.updateById(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }

    @Override
    public int auditStockCheck(Long id) {
        StockCheck stockCheck = new StockCheck();
        stockCheck.setId(id);
        stockCheck.setStatus(1); // 已审核
        return this.updateById(stockCheck) ? 1 : 0;
    }
}
