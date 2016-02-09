package com.leolimajr.sample.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Leo Lima
 */

@SpringBootApplication
@ComponentScan("com.leolimajr.sample")
@EntityScan("com.leolimajr.sample.domain")
@EnableJpaRepositories("com.leolimajr.sample.repository")
@EnableCaching
@Profile({ ApplicationConfig.ConfigProfile.MAIN,
		ApplicationConfig.ConfigProfile.ACCEPTANCE_TEST,
		ApplicationConfig.ConfigProfile.INTEGRATION_TEST,
		ApplicationConfig.ConfigProfile.WEB_INTEGRATION_TEST })
public class ApplicationConfig extends SpringBootServletInitializer {

	public interface ConfigProfile {
		public static final String MAIN = "main";
		public static final String INTEGRATION_TEST = "integration-test";
		public static final String WEB_INTEGRATION_TEST = "web-integration-test";
		public static final String ACCEPTANCE_TEST = "acceptance-test";
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApplicationConfig.class);
	}

	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder().sources(ApplicationConfig.class).run(args);
	}
}
