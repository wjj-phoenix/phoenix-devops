package com.phoenix.devops.service;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.model.vo.SysRoleVO;

import java.util.List;

/**
 *  服务层。
 *
 * @author wjj-phoenix
 * @since 2025-02-17
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 查询所有角色信息
     *
     * @return 角色信息列表
     */
    List<SysRole> fetchAllSysRoles();

    /**
     * 分页查询所有角色信息
     *
     * @return 角色信息列表
     */
    IPage<SysRoleVO> fetchAllSysRolesByPage(Integer page, Integer size, String condition);

    /**
     * 根据用户名查询角色信息
     *
     * @param username 用户名
     * @return 角色信息
     */
    List<SysRole> fetchSysRolesByUsername(String username);

    /**
     * 根据主键ID查询角色信息
     *
     * @param id 主键ID
     * @return 角色信息
     */
    SysRole fetchSysRoleById(Long id);

    /**
     * 添加角色信息
     *
     * @param roleVO 角色信息
     * @return 主键ID
     */
    Long addSysRole(SysRoleVO roleVO);

    /**
     * 根据主键ID修改角色信息
     *
     * @param id     主键ID
     * @param roleVO 角色信息
     * @return true|false
     */
    Boolean modSysRole(Long id, SysRoleVO roleVO);

    /**
     * 根据主键删除角色信息
     *
     * @param id 主键列表
     * @return true|false
     */
    Boolean delSysRole(Long id);
}
