package uk.co.scottishpower.smartreads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uk.co.scottishpower.smartreads.config.propertysource.RsaKeyProperties;
import uk.co.scottishpower.smartreads.config.propertysource.BasicAuthProperties;

@EnableConfigurationProperties({RsaKeyProperties.class, BasicAuthProperties.class})
@SpringBootApplication
public class SmartReadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartReadsApplication.class, args);
	}

}
