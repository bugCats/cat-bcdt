package cc.bugcat.bcdt.logsev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;


/**
 * @author bugcat-cc
 * */
@EnableWebFlux
@EnableScheduling
@SpringBootApplication(scanBasePackages = "cc.bugcat")
public class BcdtLogsevApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BcdtLogsevApplication.class);
		app.run(args);
	}

}
