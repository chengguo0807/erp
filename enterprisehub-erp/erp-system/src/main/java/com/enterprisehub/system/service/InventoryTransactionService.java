package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.InventoryTransaction;

public interface InventoryTransactionService {
    PageResult<InventoryTransaction> selectPage(PageQuery pageQuery, Long productId, Long warehouseId);
    InventoryTransaction selectById(Long id);
    boolean insert(InventoryTransaction entity);
    boolean update(InventoryTransaction entity);
    boolean deleteById(Long id);
}
