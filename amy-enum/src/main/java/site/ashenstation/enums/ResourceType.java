package site.ashenstation.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResourceType {

    GET("PICTURE");

    private final String type;

    @EnumValue
    @JsonValue
    public String getType() {
        return type;
    }
}
