package org.qinyu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class XiaoqinMenuBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoqinMenuBackendApplication.class, args);
        log.info("用户服务启动成功！端口号：{}", 8101);
    }

}
