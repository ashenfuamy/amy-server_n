package site.ashenstation.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationArchiveStatus {

    TO_BE_RELEASED(0),
    RELEASED(1);

    @EnumValue
    private final Integer type;
}
