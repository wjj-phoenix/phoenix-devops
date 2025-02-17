package com.phoenix.devops.config;

import com.mybatisflex.core.audit.AuditManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Configuration
public class IMyBatisFlexConfiguration {
    private static final Logger logger = LoggerFactory.getLogger("mybatis-flex-sql");

    public IMyBatisFlexConfiguration() {
        AuditManager.setAuditEnable(true);
        // 设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage -> logger.info("{}, {}ms, count: {}", auditMessage.getFullSql(), auditMessage.getElapsedTime(), auditMessage.getQueryCount()));
    }
}
