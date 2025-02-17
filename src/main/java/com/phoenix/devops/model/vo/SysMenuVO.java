package com.phoenix.devops.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 实体类。
 *
 * @author wjj-phoenix
 * @since 2025-02-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 父ID
     */
    @Schema(description = "父ID")
    private Long parentId;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    private String code;

    /**
     * 请求路径
     */
    @Schema(description = "请求路径")
    private String url;

    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    private String method;

    /**
     * 类型
     */
    @Schema(description = "菜单类型", defaultValue = "1")
    private Integer type;

    /**
     * 排序
     */
    @Schema(description = "排序", defaultValue = "100")
    private Integer sort;

    /**
     * 外链地址
     */
    @Schema(description = "外链")
    private String redirect;

    /**
     * 是否隐藏
     */
    @Schema(description = "是否隐藏", defaultValue = "false")
    private Boolean hidden;

    /**
     * 描述信息
     */
    @Schema(description = "描述信息")
    private String description;
}
