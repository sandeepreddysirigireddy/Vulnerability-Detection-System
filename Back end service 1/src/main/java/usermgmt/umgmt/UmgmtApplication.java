package usermgmt.umgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class UmgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(UmgmtApplication.class, args);
	}

}
