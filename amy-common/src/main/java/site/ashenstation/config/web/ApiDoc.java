package site.ashenstation.config.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.ashenstation.properties.ApiDocProperties;

@Configuration
@RequiredArgsConstructor
public class ApiDoc {
    private final ApiDocProperties apiDocProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiDocProperties.getTitle())
                        .description(apiDocProperties.getDescription())
                        .version(apiDocProperties.getVersion())
                        .contact(new Contact().email(apiDocProperties.getContact().getEmail()).name(apiDocProperties.getContact().getName()).url(apiDocProperties.getContact().getUrl()))
                );
    }
}
