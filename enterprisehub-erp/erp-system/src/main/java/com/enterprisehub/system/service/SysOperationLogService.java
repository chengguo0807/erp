package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.SysOperationLog;

public interface SysOperationLogService {
    PageResult<SysOperationLog> selectPage(PageQuery pageQuery);
    SysOperationLog selectById(Long id);
    boolean insert(SysOperationLog entity);
    boolean deleteById(Long id);
}
