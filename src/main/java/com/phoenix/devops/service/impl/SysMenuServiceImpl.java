package com.phoenix.devops.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.annotation.RecordLog;
import com.phoenix.devops.common.SelectCommon;
import com.phoenix.devops.entity.SysMenu;
import com.phoenix.devops.enums.OperationType;
import com.phoenix.devops.mapper.SysMenuMapper;
import com.phoenix.devops.model.vo.SysMenuVO;
import com.phoenix.devops.service.ISysMenuService;
import com.phoenix.devops.service.ISysRoleMenuService;
import com.phoenix.devops.utils.TreeUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.phoenix.devops.entity.table.SysAccountRoleTableDef.SYS_ACCOUNT_ROLE;
import static com.phoenix.devops.entity.table.SysAccountTableDef.SYS_ACCOUNT;
import static com.phoenix.devops.entity.table.SysMenuTableDef.SYS_MENU;
import static com.phoenix.devops.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;
import static com.phoenix.devops.entity.table.SysRoleTableDef.SYS_ROLE;

/**
 * 服务层实现。
 *
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Resource
    private ISysRoleMenuService roleMenuService;

    @Override
    public List<SysMenuVO> fetchAllSysMenus(String condition) {
        List<SysMenu> menus = new SelectCommon<SysMenu>().findAll(condition, this);
        return TreeUtil.generateTrees(BeanUtil.copyToList(menus, SysMenuVO.class));
    }

    @Override
    public List<SysMenuVO> fetchSysMenusByPId(Long pid) {
        List<SysMenu> menus = this.list(QueryWrapper.create().select(SYS_MENU.DEFAULT_COLUMNS).from(SYS_MENU).where(SYS_MENU.PARENT_ID.eq(pid)));
        return BeanUtil.copyToList(menus, SysMenuVO.class);
    }

    @Override
    public List<SysMenu> fetchMenusByUsername(String username) {
        return this.list(
                QueryWrapper.create()
                        .select(SYS_MENU.DEFAULT_COLUMNS).from(SYS_MENU)
                        .leftJoin(SYS_ROLE_MENU).on(SYS_MENU.ID.eq(SYS_ROLE_MENU.MENU_ID))
                        .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ACCOUNT_ROLE.ROLE_ID.eq(SYS_ROLE_MENU.ROLE_ID))
                        .leftJoin(SYS_ACCOUNT).on(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(SYS_ACCOUNT.ID))
                        .where(SYS_ACCOUNT.USERNAME.eq(username))
        );
    }

    @Override
    public List<SysMenu> fetchSysMenusByRoleId(Long roleId) {
        return this.list(
                QueryWrapper.create()
                        .select(SYS_MENU.DEFAULT_COLUMNS).from(SYS_MENU)
                        .leftJoin(SYS_ROLE_MENU).on(SYS_MENU.ID.eq(SYS_ROLE_MENU.MENU_ID))
                        .where(SYS_ROLE_MENU.ROLE_ID.eq(roleId))
        );
    }

    @Override
    public List<SysMenu> fetchMenusByRoleCode(String code) {
        return this.list(
                QueryWrapper.create()
                        .select(SYS_MENU.DEFAULT_COLUMNS).from(SYS_MENU)
                        .leftJoin(SYS_ROLE_MENU).on(SYS_MENU.ID.eq(SYS_ROLE_MENU.MENU_ID))
                        .leftJoin(SYS_ROLE).on(SYS_ROLE.ID.eq(SYS_ROLE_MENU.ROLE_ID))
                        .where(SYS_ROLE.CODE.eq(code))
        );
    }

    @Override
    public SysMenu fetchSysMenuById(Long id) {
        return this.getById(id);
    }

    @Override
    @RecordLog(title = "添加菜单信息", service = ISysMenuService.class, type = OperationType.INSERT, isCompare = false)
    public Long addSysMenu(SysMenuVO menuVO) {
        SysMenu menu = BeanUtil.toBean(menuVO, SysMenu.class);
        if (!this.save(menu)) {
            throw new IllegalStateException("添加菜单信息失败");
        }
        return menu.getId();
    }

    @Override
    @RecordLog(title = "修改菜单信息", service = ISysMenuService.class)
    public Boolean modSysMenu(Long id, SysMenuVO menuVO) {
        SysMenu menu = BeanUtil.toBean(menuVO, SysMenu.class);
        menu.setId(id);
        if (!this.updateById(menu)) {
            throw new IllegalStateException("修改菜单信息失败");
        }
        return true;
    }

    @Override
    @RecordLog(title = "删除菜单信息", service = ISysMenuService.class, isCompare = false, type = OperationType.DELETE)
    public Boolean delSysMenu(Long id) {
        long count = roleMenuService.count(QueryWrapper.create().where(SYS_ROLE_MENU.MENU_ID.eq(id)));
        if (count > 0) {
            throw new IllegalStateException("菜单已被角色关联，不能删除");
        }
        if (!this.removeById(id)) {
            throw new IllegalStateException("删除菜单信息失败");
        }
        return true;
    }
}
