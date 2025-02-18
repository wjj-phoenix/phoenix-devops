package com.phoenix.devops.controller;

import com.phoenix.devops.model.Add;
import com.phoenix.devops.model.Mod;
import com.phoenix.devops.model.vo.SysMenuVO;
import com.phoenix.devops.service.ISysMenuService;
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
@RequestMapping("/menu")
public class SysMenuController {

    @Resource
    private ISysMenuService service;

    /**
     * 添加。
     *
     * @param menuVO 菜单信息
     * @return 菜单主键ID
     */
    @PostMapping()
    public long save(@Validated({Add.class}) @RequestBody SysMenuVO menuVO) {
        return service.addSysMenu(menuVO);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/{id}")
    public boolean remove(@PathVariable Long id) {
        return service.delSysMenu(id);
    }

    /**
     * 根据主键更新。
     *
     * @param menuVO 菜单信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @Validated({Mod.class}) @RequestBody SysMenuVO menuVO) {
        return service.modSysMenu(id, menuVO);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping()
    public List<SysMenuVO> list(@RequestParam(name = "condition", required = false, defaultValue = "") String condition) {
        return service.fetchAllSysMenus(condition);
    }
}
