package com.phoenix.devops.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.annotation.RecordLog;
import com.phoenix.devops.common.SelectCommon;
import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.entity.SysRoleMenu;
import com.phoenix.devops.enums.OperationType;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.mapper.SysRoleMapper;
import com.phoenix.devops.model.vo.SysAccountVO;
import com.phoenix.devops.model.vo.SysRoleVO;
import com.phoenix.devops.service.ISysAccountRoleService;
import com.phoenix.devops.service.ISysAccountService;
import com.phoenix.devops.service.ISysRoleMenuService;
import com.phoenix.devops.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.phoenix.devops.entity.table.SysAccountRoleTableDef.SYS_ACCOUNT_ROLE;
import static com.phoenix.devops.entity.table.SysAccountTableDef.SYS_ACCOUNT;
import static com.phoenix.devops.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;
import static com.phoenix.devops.entity.table.SysRoleTableDef.SYS_ROLE;

/**
 * 服务层实现。
 *
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private ISysAccountService accountService;
    @Resource
    private ISysAccountRoleService accountRoleService;
    @Resource
    private ISysRoleMenuService roleMenuService;

    @Override
    public List<SysRole> fetchAllSysRoles() {
        return this.list();
    }

    @Override
    public IPage<SysRoleVO> fetchAllSysRolesByPage(Integer page, Integer size, String condition) {
        Page<SysRole> rolePage = new SelectCommon<SysRole>().findAll(page, size, condition, this);
        List<SysRoleVO> roleVOS = BeanUtil.copyToList(rolePage.getRecords(), SysRoleVO.class);
        if (CollUtil.isNotEmpty(roleVOS)) {
            roleVOS.forEach(roleVO -> {
                roleVO.setAccounts(
                        new HashSet<>(
                                BeanUtil.copyToList(
                                        accountService.list(
                                                QueryWrapper.create()
                                                        .select(SYS_ACCOUNT.DEFAULT_COLUMNS).from(SYS_ACCOUNT)
                                                        .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(SYS_ACCOUNT.ID))
                                                        .where(SYS_ACCOUNT_ROLE.ROLE_ID.eq(roleVO.getId()))
                                        ), SysAccountVO.class
                                )
                        )
                );

                roleVO.setMenuIds(
                        roleMenuService.list(
                                QueryWrapper.create()
                                        .select(SYS_ROLE_MENU.DEFAULT_COLUMNS)
                                        .from(SYS_ROLE_MENU)
                                        .where(SYS_ROLE_MENU.ROLE_ID.eq(roleVO.getId()))
                        ).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet())
                );
            });
            return IPage.<SysRoleVO>builder()
                    .page(rolePage.getPageNumber())
                    .size(rolePage.getPageSize())
                    .total(rolePage.getTotalRow())
                    .rows(roleVOS)
                    .build();
        }
        return IPage.empty();
    }

    @Override
    public List<SysRole> fetchSysRolesByUsername(String username) {
        return this.list(
                QueryWrapper.create()
                        .select(SYS_ROLE.DEFAULT_COLUMNS).from(SYS_ROLE)
                        .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ROLE.ID.eq(SYS_ACCOUNT_ROLE.ROLE_ID))
                        .leftJoin(SYS_ACCOUNT).on(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(SYS_ACCOUNT.ID))
                        .where(SYS_ACCOUNT.USERNAME.eq(username))
        );
    }

    @Override
    public SysRole fetchSysRoleById(Long id) {
        return this.getById(id);
    }

    @Override
    @RecordLog(title = "新增角色信息", service = ISysRoleService.class, type = OperationType.INSERT, isCompare = false)
    public Long addSysRole(SysRoleVO roleVO) {
        SysRole role = BeanUtil.toBean(roleVO, SysRole.class);
        if (!this.save(role)) {
            throw new IllegalStateException("新增角色失败");
        }
        return role.getId();
    }

    @Override
    @RecordLog(title = "修改角色信息", service = ISysRoleService.class)
    public Boolean modSysRole(Long id, SysRoleVO roleVO) {
        SysRole role = BeanUtil.toBean(roleVO, SysRole.class);
        role.setId(id);
        if (!this.updateById(role)) {
            throw new IllegalStateException("修改角色失败");
        }
        return true;
    }

    @Override
    @RecordLog(title = "删除角色", service = ISysRoleService.class, type = OperationType.DELETE, isCompare = false)
    public Boolean delSysRole(Long id) {
        long count = accountRoleService.count(QueryWrapper.create().where(SYS_ACCOUNT_ROLE.ROLE_ID.eq(id)));
        if (count > 0) {
            throw new IllegalStateException("角色已被账号关联，不能删除");
        }
        if (!this.removeById(id)) {
            throw new IllegalStateException("新增角色失败");
        }
        return true;
    }
}
