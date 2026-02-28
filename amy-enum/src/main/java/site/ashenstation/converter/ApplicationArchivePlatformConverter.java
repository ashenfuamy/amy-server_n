package site.ashenstation.converter;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.ashenstation.enums.ApplicationArchivePlatform;

@Component
public class ApplicationArchivePlatformConverter implements Converter<String, ApplicationArchivePlatform> {
    
    @Override
    public @Nullable ApplicationArchivePlatform convert(String source) {
        return ApplicationArchivePlatform.fromValue(source);
    }
}
