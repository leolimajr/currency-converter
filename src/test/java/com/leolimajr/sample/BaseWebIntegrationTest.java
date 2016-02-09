package com.leolimajr.sample;

import com.leolimajr.sample.config.ApplicationConfig;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Leo Lima
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationConfig.class)
@WebIntegrationTest({ "server.port=0" })
@DirtiesContext
@ActiveProfiles(ApplicationConfig.ConfigProfile.WEB_INTEGRATION_TEST)
@Sql(scripts = "/dml-login.sql")
public abstract class BaseWebIntegrationTest {
	@Value("${local.server.port}")
	protected int port;
}
