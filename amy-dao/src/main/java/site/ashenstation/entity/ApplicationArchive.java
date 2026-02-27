package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import site.ashenstation.enums.ApplicationArchiveStatus;

@Data
@Table("sys_application_archive")
public class ApplicationArchive {
    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    private String id;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 根路径
     */
    private String rootPath;

    /**
     * 版本号
     */
    private String version;

    /**
     * 状态
     */
    private ApplicationArchiveStatus status;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 是否最新版本（1-是，0-否）
     */
    private Integer isLatest;

    /**
     * 平台（如Android、iOS、Windows等）
     */
    private String platform;

    /**
     * 架构（如x86、arm64等）
     */
    private String arch;
}
