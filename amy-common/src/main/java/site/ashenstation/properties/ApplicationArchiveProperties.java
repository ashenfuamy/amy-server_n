package site.ashenstation.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "application-archive")
public class ApplicationArchiveProperties implements InitializingBean {
    private static String rootPath;
    private String uriPath;
    private List<Application> applications;

    @Value("${application-archive.root-path}")
    public void setRootPath(String rootPath) {
        ApplicationArchiveProperties.rootPath = rootPath;
    }

    public Application getApplication(String applicationName) {
        for (Application application : applications) {
            if (application.getName().equals(applicationName)) {
                return application;
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (rootPath == null) {
            throw new RuntimeException("rootPath is required");
        }

        for (Application application : applications) {
            String directory = application.getDirectory();
            if (directory == null) {
                throw new RuntimeException("directory is required");
            }

            File file = new File(rootPath, directory);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    @Data
    public static class Application {
        private String name;
        private String directory;

        private static File currentWorkingDirectory;

        public File getDirectoryFile() {
            return new File(rootPath, directory);
        }
    }
}
