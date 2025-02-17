package com.phoenix.devops.lang;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 统一分页结构类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class IPage<T> {
    @Parameter(description = "当前页码")
    private long page;

    @Parameter(description = "每页数据量大小")
    private long size;

    @Parameter(description = "总页码")
    private long pages;

    @Parameter(description = "总的数据量")
    private long total;

    @Parameter(description = "数据")
    private List<T> rows;
}
