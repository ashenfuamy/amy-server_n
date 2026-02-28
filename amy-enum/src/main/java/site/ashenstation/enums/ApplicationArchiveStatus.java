package site.ashenstation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationArchiveStatus {

    TO_BE_RELEASED(0),
    RELEASED(1);

    private final Integer type;
}
