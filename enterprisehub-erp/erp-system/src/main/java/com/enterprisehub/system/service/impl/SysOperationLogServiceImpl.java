package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.SysOperationLog;
import com.enterprisehub.system.mapper.SysOperationLogMapper;
import com.enterprisehub.system.service.SysOperationLogService;
import org.springframework.stereotype.Service;

@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {
    
    @Override
    public PageResult<SysOperationLog> selectPage(PageQuery pageQuery) {
        Page<SysOperationLog> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<SysOperationLog> result = this.page(page);
        return PageResult.build(result);
    }
    
    @Override
    public SysOperationLog selectById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean insert(SysOperationLog entity) {
        return this.save(entity);
    }
    
    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
