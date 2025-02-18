package com.phoenix.devops.service;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.entity.SysMenu;
import com.phoenix.devops.model.vo.SysMenuVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 *  服务层。
 *
 * @author wjj-phoenix
 * @since 2025-02-17
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 分页查询所有菜单项
     *
     * @return 菜单项列表
     */
    List<SysMenuVO> fetchAllSysMenus(String condition);

    /**
     * 根据父ID查询其子菜单项
     *
     * @param pid 父ID
     * @return 子菜单项列表
     */
    List<SysMenuVO> fetchSysMenusByPId(@NotNull(message = "父ID不能为空!") Long pid);

    /**
     * 根据用户名查询菜单信息
     *
     * @param username 用户名
     * @return 菜单列表
     */
    List<SysMenu> fetchMenusByUsername(@NotNull(message = "用户名不能为空!") String username);

    /**
     * 根据角色ID查询菜单项
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> fetchSysMenusByRoleId(@NotNull(message = "角色ID不能为空!") Long roleId);

    /**
     * 根据角色编码查询菜单项
     *
     * @param code 角色编码
     * @return 菜单列表
     */
    List<SysMenu> fetchMenusByRoleCode(@NotNull(message = "角色编码不能为空!") String code);

    /**
     * 根据主键ID查询菜单项
     *
     * @param id 主键ID
     * @return 菜单项
     */
    SysMenu fetchSysMenuById(@NotNull(message = "主键ID不能为空!") Long id);

    /**
     * 添加菜单项
     *
     * @param menuVO 菜单项信息
     * @return 主键ID
     */
    Long addSysMenu(@NotNull(message = "菜单信息不能为空!") SysMenuVO menuVO);

    /**
     * 根据主键ID修改菜单信息
     *
     * @param id     主键ID
     * @param menuVO 菜单信息
     * @return true|false
     */
    Boolean modSysMenu(@NotNull(message = "菜单主键ID不能为空!") Long id, @NotNull(message = "菜单信息不能为空!") SysMenuVO menuVO);

    /**
     * 根据主键ID删除菜单项
     *
     * @param id 主键ID列表
     * @return true|false
     */
    Boolean delSysMenu(@NotNull(message = "菜单主键ID不能为空!") Long id);
}
