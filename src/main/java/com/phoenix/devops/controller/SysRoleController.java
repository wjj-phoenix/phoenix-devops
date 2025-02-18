package com.phoenix.devops.controller;

import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.model.Add;
import com.phoenix.devops.model.Mod;
import com.phoenix.devops.model.vo.SysRoleVO;
import com.phoenix.devops.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制层。
 *
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Valid
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Resource
    private ISysRoleService service;

    /**
     * 添加。
     *
     * @param roleVO 角色信息
     * @return 角色主键ID
     */
    @PostMapping()
    public long addSysRole(@Validated({Add.class}) @RequestBody SysRoleVO roleVO) {
        return service.addSysRole(roleVO);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/{id}")
    public boolean delSysRole(@PathVariable Long id) {
        return service.delSysRole(id);
    }

    /**
     * 根据主键更新。
     *
     * @param roleVO 角色信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/{id}")
    public boolean modSysRole(@PathVariable Long id, @Validated({Mod.class}) @RequestBody SysRoleVO roleVO) {
        return service.modSysRole(id, roleVO);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("all")
    public List<SysRole> fetchAllSysRoles() {
        return service.fetchAllSysRoles();
    }

    /**
     * 分页查询。
     *
     * @param num       页码
     * @param size      每页大小
     * @param condition 条件
     * @return 分页对象
     */
    @GetMapping()
    @Operation(summary = "分页查询角色信息",
            description = "根据条件【条件可有可无】分页查询角色信息",
            parameters = {
                    @Parameter(name = "num", description = "页码"),
                    @Parameter(name = "size", description = "每页大小"),
                    @Parameter(name = "condition", description = "条件")
            }
    )
    public IPage<SysRoleVO> fetchAllSysRolesByPage(
            @RequestParam(value = "num", required = false, defaultValue = "1") Integer num,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "condition", defaultValue = "", required = false) String condition
    ) {
        return service.fetchAllSysRolesByPage(num, size, condition);
    }
}
