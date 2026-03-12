package site.ashenstation.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum MosaicType {
    HAS("has"),
    NO("no");

    @EnumValue
    private String value;

    MosaicType(String value) {
    }
}
