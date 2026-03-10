package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.system.domain.entity.SysDept;

import java.util.List;

/**
 * 部门服务接口
 */
public interface SysDeptService extends IService<SysDept> {
    
    /**
     * 查询部门列表（树形结构）
     */
    List<SysDept> selectDeptList(String deptName);
    
    /**
     * 根据ID查询部门
     */
    SysDept selectDeptById(Long id);
    
    /**
     * 新增部门
     */
    int insertDept(SysDept dept);
    
    /**
     * 修改部门
     */
    int updateDept(SysDept dept);
    
    /**
     * 删除部门
     */
    int deleteDeptById(Long id);
    
    /**
     * 构建部门树形结构
     */
    List<SysDept> buildDeptTree(List<SysDept> depts);
    
    /**
     * 查询部门树形结构
     */
    List<SysDept> selectDeptTree();
}
