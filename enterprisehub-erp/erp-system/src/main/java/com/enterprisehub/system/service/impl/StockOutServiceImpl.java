package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.StockOut;
import com.enterprisehub.system.mapper.StockOutMapper;
import com.enterprisehub.system.service.StockOutService;
import org.springframework.stereotype.Service;

/**
 * 出库单服务实现类
 */
@Service
public class StockOutServiceImpl extends ServiceImpl<StockOutMapper, StockOut> implements StockOutService {

    @Override
    public PageResult<StockOut> selectPage(PageQuery pageQuery) {
        Page<StockOut> page = this.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
        return PageResult.build(page);
    }

    @Override
    public StockOut selectById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean insert(StockOut entity) {
        return this.save(entity);
    }

    @Override
    public boolean update(StockOut entity) {
        return this.updateById(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }

    @Override
    public int auditStockOut(Long id) {
        StockOut stockOut = new StockOut();
        stockOut.setId(id);
        stockOut.setStatus(1); // 已审核
        return this.updateById(stockOut) ? 1 : 0;
    }
}
