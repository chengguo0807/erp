package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    
    /**
     * 根据角色ID查询菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 删除角色的所有菜单
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 批量插入角色菜单
     */
    int batchInsert(@Param("list") List<SysRoleMenu> list);
}
