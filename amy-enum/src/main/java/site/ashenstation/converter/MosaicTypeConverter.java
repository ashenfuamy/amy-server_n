package site.ashenstation.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.ashenstation.enums.MosaicType;

@Component
public class MosaicTypeConverter implements Converter<String, MosaicType> {
    @Override
    public MosaicType convert(String source) {
        return MosaicType.find(Integer.valueOf(source));
    }
}
