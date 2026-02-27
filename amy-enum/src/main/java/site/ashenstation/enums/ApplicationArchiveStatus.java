package site.ashenstation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationArchiveStatus {

    Authorization("2");

    private final String type;
}
