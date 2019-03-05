package shenzhen.teamway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("shenzhen.teamway.mapper")
//允许定时任务
@EnableScheduling
public class OnvifserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnvifserviceApplication.class, args);
    }

}
