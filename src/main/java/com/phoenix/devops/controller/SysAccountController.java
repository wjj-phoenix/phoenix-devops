package com.phoenix.devops.controller;

import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.model.Add;
import com.phoenix.devops.model.Mod;
import com.phoenix.devops.model.vo.SysAccountVO;
import com.phoenix.devops.service.ISysAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 控制层。
 *
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Valid
@RestController
@RequestMapping("/account")
public class SysAccountController {

    @Autowired
    private ISysAccountService service;

    /**
     * 添加。
     *
     * @param accountVO 用户信息
     * @return 主键ID
     */
    @PostMapping()
    public long save(@Validated({Add.class}) @RequestBody SysAccountVO accountVO) {
        return service.addSysAccount(accountVO);
    }

    /**
     * 根据主键删除。
     *
     * @param ids 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping()
    public boolean delSysAccount(@RequestBody Set<Long> ids) {
        return service.delSysAccount(ids);
    }

    /**
     * 根据主键更新。
     *
     * @param accountVO 用户信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/{id}")
    public boolean modSysAccount(@PathVariable Long id, @Validated({Mod.class}) @RequestBody SysAccountVO accountVO) {
        return service.modSysAccount(id, accountVO);
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param username 用户名
     * @return 详情
     */
    @GetMapping("/info")
    public SysAccount getInfo(@RequestParam String username) {
        return service.fetchSysAccountWithRelationsByUsername(username);
    }

    /**
     * 分页查询。
     *
     * @param num       页码 分页对象
     * @param size      每页大小
     * @param condition 条件
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(summary = "分页查询用户信息",
            description = "根据条件【条件可有可无】分页查询用户信息",
            parameters = {
                    @Parameter(name = "num", description = "页码"),
                    @Parameter(name = "size", description = "每页大小"),
                    @Parameter(name = "condition", description = "条件")
            }
    )
    public IPage<SysAccountVO> page(
            @RequestParam(value = "num", required = false, defaultValue = "1") Integer num,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "condition", defaultValue = "", required = false) String condition
    ) {
        return service.fetchAllAccountsByCondition(num, size, condition);
    }
}
