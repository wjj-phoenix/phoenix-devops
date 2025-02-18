package com.phoenix.devops.model.vo;

import com.phoenix.devops.annotation.Immutable;
import com.phoenix.devops.annotation.UnionUnique;
import com.phoenix.devops.annotation.Unique;
import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.model.Add;
import com.phoenix.devops.model.Mod;
import com.phoenix.devops.service.ISysAccountService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author wjj-phoenix
 * @since 2025-02-10
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@UnionUnique.List({
        @UnionUnique(service = ISysAccountService.class, fields = {"username", "email"}, message = "用户名和邮箱的组合值已存在", groups = {Add.class, Mod.class})
})
public class SysAccountVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空", groups = {Add.class})
    @Immutable(message = "用户名字段不能修改", groups = {Mod.class})
    @Unique(service = ISysAccountService.class, field = "username", message = "用户名已存在")
    private String username;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空", groups = {Add.class})
    private String email;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "备注")
    private String remark;

    @NotNull(message = "用户状态不能为空", groups = {Add.class, Mod.class})
    @Schema(description = "是否被锁", defaultValue = "1")
    private Integer locked;

    @NotNull(message = "用户状态不能为空", groups = {Add.class, Mod.class})
    @Schema(description = "是否可用", defaultValue = "1")
    private Integer enabled;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    @Schema(description = "最新登录时间")
    private LocalDateTime latestLoginTime;

    @Schema(description = "创建用户")
    private Long createdUser;

    @Schema(description = "角色ID列表")
    private Set<Long> roleIds;

    @Schema(description = "角色编码列表")
    private Set<SysRole> roles;
}
