package com.phoenix.devops.model.vo;

import com.phoenix.devops.utils.TreeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
public class SysMenuVO implements Serializable, TreeUtil.TreeNode<Long, SysMenuVO> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
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

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    /**
     * 子菜单信息
     */
    @Schema(description = "子菜单信息")
    private List<SysMenuVO> children;

    @Override
    public boolean root() {
        return Objects.equals(this.parentId, 0L);
    }
}
