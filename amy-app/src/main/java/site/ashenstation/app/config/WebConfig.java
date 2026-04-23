package site.ashenstation.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.ashenstation.properties.StaticResourceProperties;

import java.io.File;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StaticResourceProperties staticResourceProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String avatarUtl = "file:" + staticResourceProperties.getAvatarRoot().replace("\\", "/") + "/";
        String posterUtl = "file:" + staticResourceProperties.getPosterRoot().replace("\\", "/") + "/";

        registry.addResourceHandler(staticResourceProperties.getAvatarResourcePrefix() + "/**").addResourceLocations(avatarUtl);
        registry.addResourceHandler(staticResourceProperties.getPosterResourcePrefix() + "/**").addResourceLocations(posterUtl);

        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(staticResourceProperties.getVideoResourcePrefix() + "/**");

        staticResourceProperties.getVideoRoots().forEach(videoRootProperties -> {
            File file = new File(videoRootProperties.getPath());
            resourceHandlerRegistration.addResourceLocations("file:" + file.getAbsolutePath().replace("\\", "/") + "/");
        });
    }
}
