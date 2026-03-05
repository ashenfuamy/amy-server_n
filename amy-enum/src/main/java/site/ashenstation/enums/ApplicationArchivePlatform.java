package site.ashenstation.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationArchivePlatform {
    WIN("win32"),
    LINUX("linux"),
    MAC("mac");

    @EnumValue
    private final String type;

    // 核心：根据字符串值（忽略大小写）匹配枚举
    public static ApplicationArchivePlatform fromValue(String value) {

        if (value == null) {
            return null;
        }
        // 遍历枚举，忽略大小写匹配
        for (ApplicationArchivePlatform platform : ApplicationArchivePlatform.values()) {
            if (platform.type.equalsIgnoreCase(value)) {
                return platform;
            }
        }
        // 匹配失败时抛出异常，让Spring捕获
        return null;
    }

    // 获取枚举的字符串值
    public String getValue() {
        return type;
    }
}
