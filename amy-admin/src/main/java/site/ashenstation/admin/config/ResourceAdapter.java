package site.ashenstation.admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.ashenstation.admin.config.properties.ApplicationArchiveProperties;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class ResourceAdapter implements WebMvcConfigurer {

    private final ApplicationArchiveProperties applicationArchiveProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String archive = "file:" + ApplicationArchiveProperties.getRootPath().replace("\\", "/") + "/";

        registry.addResourceHandler(applicationArchiveProperties.getUriPath() + "/**").addResourceLocations(archive);
    }
}
