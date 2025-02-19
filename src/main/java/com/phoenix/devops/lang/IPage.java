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

    public static <T> IPage<T> of(long page, long size, long pages, long total, List<T> rows) {
        return IPage.<T>builder().page(page).size(size).pages(pages).total(total).rows(rows).build();
    }

    public static <T> IPage<T> empty() {
        return of(0, 0, 0, 0, null);
    }
}
