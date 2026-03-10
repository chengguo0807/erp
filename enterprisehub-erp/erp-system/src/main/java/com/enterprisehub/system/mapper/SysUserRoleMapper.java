package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    
    /**
     * 根据用户ID查询角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
    
    /**
     * 删除用户的所有角色
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 批量插入用户角色
     */
    int batchInsert(@Param("list") List<SysUserRole> list);
}
