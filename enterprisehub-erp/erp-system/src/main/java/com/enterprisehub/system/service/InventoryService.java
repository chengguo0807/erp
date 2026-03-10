package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.dto.InventoryTransferDTO;
import com.enterprisehub.system.domain.entity.Inventory;

import java.util.List;

/**
 * 库存服务接口
 */
public interface InventoryService extends IService<Inventory> {
    
    /**
     * 分页查询库存列表
     */
    PageResult<Inventory> selectPage(PageQuery pageQuery, Long productId, Long warehouseId);
    
    /**
     * 根据ID查询库存详情
     */
    Inventory selectById(Long id);
    
    /**
     * 新增库存
     */
    boolean insert(Inventory entity);
    
    /**
     * 修改库存
     */
    boolean update(Inventory entity);
    
    /**
     * 删除库存
     */
    boolean deleteById(Long id);
    
    /**
     * 获取库存预警列表
     */
    List<Inventory> getWarningList();
    
    /**
     * 库存调拨
     */
    int transfer(InventoryTransferDTO transferDTO);
}
