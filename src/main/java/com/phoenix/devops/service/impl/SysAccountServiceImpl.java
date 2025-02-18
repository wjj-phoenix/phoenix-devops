package com.phoenix.devops.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.annotation.RecordLog;
import com.phoenix.devops.common.SelectCommon;
import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.entity.SysAccountRole;
import com.phoenix.devops.enums.OperationType;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.mapper.SysAccountMapper;
import com.phoenix.devops.model.vo.PasswordVO;
import com.phoenix.devops.model.vo.SysAccountVO;
import com.phoenix.devops.service.ISysAccountRoleService;
import com.phoenix.devops.service.ISysAccountService;
import com.phoenix.devops.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.phoenix.devops.entity.table.SysAccountRoleTableDef.SYS_ACCOUNT_ROLE;
import static com.phoenix.devops.entity.table.SysAccountTableDef.SYS_ACCOUNT;
import static com.phoenix.devops.entity.table.SysRoleTableDef.SYS_ROLE;

/**
 * 服务层实现。
 *
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Service
public class SysAccountServiceImpl extends ServiceImpl<SysAccountMapper, SysAccount> implements ISysAccountService {
    @Resource
    private ISysAccountRoleService accountRoleService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public SysAccount fetchSysAccountWithRelationsByUsername(String username) {
        return mapper.selectOneWithRelationsByQuery(QueryWrapper.create().where(SYS_ACCOUNT.USERNAME.eq(username)));
    }

    @Override
    public SysAccount fetchAccountByUsername(String username) {
        return this.getOne(QueryWrapper.create().where(SYS_ACCOUNT.USERNAME.eq(username)));
    }

    @Override
    public IPage<SysAccountVO> fetchAllAccountsByCondition(Integer page, Integer limit, String condition) {
        Page<SysAccount> accountPage = new SelectCommon<SysAccount>().findAll(page, limit, condition, this);
        if (CollUtil.isNotEmpty(accountPage.getRecords())) {
            List<SysAccountVO> accountVOS = BeanUtil.copyToList(accountPage.getRecords(), SysAccountVO.class);
            accountVOS.forEach(accountVO -> {
                accountVO.setRoles(
                        new HashSet<>(
                                roleService.list(
                                        QueryWrapper.create()
                                                .select(SYS_ROLE.DEFAULT_COLUMNS)
                                                .from(SYS_ROLE)
                                                .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ACCOUNT_ROLE.ROLE_ID.eq(SYS_ROLE.ID))
                                                .where(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(accountVO.getId()))
                                )
                        )
                );
            });

        }
        return IPage.empty();
    }

    @Override
    public Long addSysAccount(SysAccountVO accountVO) {
        SysAccount account = BeanUtil.toBean(accountVO, SysAccount.class);
        if (!this.save(account)) {
            throw new IllegalStateException("添加用户信息失败");
        }
        if (accountVO.getRoleIds() != null && !accountVO.getRoleIds().isEmpty()) {
            List<SysAccountRole> ars = new ArrayList<>();
            accountVO.getRoleIds().forEach(roleId -> ars.add(new SysAccountRole(account.getId(), roleId)));
            accountRoleService.saveBatch(ars);
        }
        return account.getId();
    }

    @Override
    public Boolean modSysAccount(Long id, SysAccountVO accountVO) {
        SysAccount account = BeanUtil.toBean(accountVO, SysAccount.class);
        account.setId(id);
        if (!this.updateById(account)) {
            throw new IllegalStateException("修改用户信息失败");
        }
        return true;
    }

    @Override
    @RecordLog(title = "删除用户", service = ISysAccountService.class, type = OperationType.DELETE, isCompare = false)
    public Boolean delSysAccount(Set<Long> ids) {
        if (!this.removeByIds(ids)) {
            throw new IllegalStateException("删除用户失败");
        }
        accountRoleService.remove(QueryWrapper.create().where(SYS_ACCOUNT_ROLE.ACCOUNT_ID.in(ids)));
        return true;
    }

    @Override
    public Boolean modSysAccountPassword(Long id, PasswordVO passwordVO) {
        SysAccount account = this.getById(id);
        Assert.notNull(account, "用户不存在");
        if (!passwordEncoder.matches(passwordVO.getOldPassword(), account.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }

        if (StrUtil.equals(passwordVO.getNewPassword(), passwordVO.getOldPassword())) {
            throw new IllegalArgumentException("新密码与旧密码一致");
        }
        if (!StrUtil.equals(passwordVO.getNewPassword(), passwordVO.getConfirmPassword())) {
            throw new IllegalArgumentException("新密码与确认密码不一致");
        }
        account.setPassword(passwordEncoder.encode(passwordVO.getNewPassword()));
        if (!this.updateById(account)) {
            throw new IllegalStateException("修改密码失败");
        }
        return true;
    }
}
