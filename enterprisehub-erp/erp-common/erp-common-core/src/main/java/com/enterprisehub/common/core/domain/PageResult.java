package com.enterprisehub.common.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 当前页数据 */
    @JsonProperty("list")
    private List<T> records;

    /** 当前页码 */
    private int pageNum;

    /** 每页大小 */
    private int pageSize;

    /** 总页数 */
    private int pages;

    public PageResult() {
    }

    public PageResult(List<T> records, long total, int pageNum, int pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = (int) Math.ceil((double) total / pageSize);
    }

    /**
     * 从MyBatis-Plus的Page对象构建PageResult
     */
    public static <T> PageResult<T> build(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        return new PageResult<>(
            page.getRecords(),
            page.getTotal(),
            (int) page.getCurrent(),
            (int) page.getSize()
        );
    }
}
