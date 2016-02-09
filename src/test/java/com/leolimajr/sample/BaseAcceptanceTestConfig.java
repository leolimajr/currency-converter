package com.leolimajr.sample;

import com.gargoylesoftware.htmlunit.WebClient;
import com.leolimajr.sample.config.ApplicationConfig;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Leo Lima
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({ "server.port=0", "management.port=0" })
@DirtiesContext
@ActiveProfiles(ApplicationConfig.ConfigProfile.ACCEPTANCE_TEST)
public abstract class BaseAcceptanceTestConfig {

	@Value("${local.server.port}")
	protected int port;

	protected WebClient webClient;

	public BaseAcceptanceTestConfig() {
		this.webClient = new WebClient();
	}
}
