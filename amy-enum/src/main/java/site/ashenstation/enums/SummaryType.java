package site.ashenstation.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public enum SummaryType {
    VIDEO(1),
    SERIAL(2);

    private final Integer type;

    @EnumValue
    @JsonValue
    public Integer getType() {
        return type;
    }

    public static SummaryType find(Integer type) {
        for (SummaryType value : SummaryType.values()) {
            if (Objects.equals(value.getType(), type)) {
                return value;
            }
        }
        return null;
    }
}
