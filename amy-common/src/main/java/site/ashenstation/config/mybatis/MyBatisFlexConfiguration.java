package site.ashenstation.config.mybatis;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        AuditManager.setAuditEnable(true);

        AuditManager.setMessageCollector(auditMessage ->
                log.info("{},{}ms", auditMessage.getFullSql()
                        , auditMessage.getElapsedTime())
        );
    }
}
