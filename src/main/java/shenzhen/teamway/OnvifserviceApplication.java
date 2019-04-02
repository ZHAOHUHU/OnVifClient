package shenzhen.teamway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import shenzhen.teamway.nettyserver.UdpServer;

@SpringBootApplication
@MapperScan("shenzhen.teamway.mapper")
//允许定时任务
@EnableScheduling
public class OnvifserviceApplication implements CommandLineRunner {
@Autowired
private UdpServer udpServer;
    public static void main(String[] args) {
        SpringApplication.run(OnvifserviceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        udpServer.run(1333);
    }
}
