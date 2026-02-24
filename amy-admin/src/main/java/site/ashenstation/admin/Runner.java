package site.ashenstation.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@MapperScan("site.ashenstation.mapper")
@EnableTransactionManagement
@SpringBootApplication(
        scanBasePackages = "site.ashenstation"
)
public class Runner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Runner.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }
}
