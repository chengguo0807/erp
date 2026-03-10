package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.system.domain.entity.SysDept;
import com.enterprisehub.system.mapper.SysDeptMapper;
import com.enterprisehub.system.service.SysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDept> selectDeptList(String deptName) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(deptName), SysDept::getDeptName, deptName)
               .orderByAsc(SysDept::getSort);
        
        List<SysDept> deptList = this.list(wrapper);
        return buildDeptTree(deptList);
    }

    @Override
    public SysDept selectDeptById(Long id) {
        return this.getById(id);
    }

    @Override
    public int insertDept(SysDept dept) {
        return this.save(dept) ? 1 : 0;
    }

    @Override
    public int updateDept(SysDept dept) {
        return this.updateById(dept) ? 1 : 0;
    }

    @Override
    public int deleteDeptById(Long id) {
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDept::getId).collect(Collectors.toList());
        
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        for (SysDept n : list) {
            if (n.getParentId() != null && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public List<SysDept> selectDeptTree() {
        List<SysDept> allDepts = this.list();
        return buildDeptTree(allDepts);
    }
}
