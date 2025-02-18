package com.phoenix.devops.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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
public class SysRoleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "角色名")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "角色排序")
    private Integer sort;

    @Schema(description = "角色状态")
    private Boolean status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    @Schema(description = "有权限的菜单主键ID")
    private Set<Long> menuIds;

    @Schema(description = "角色用户")
    private Set<SysAccountVO> accounts;
}
