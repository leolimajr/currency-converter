package com.leolimajr.sample;

import com.leolimajr.sample.config.ApplicationConfig;
import org.junit.runner.RunWith;

import org.springframework.boot.test.SpringApplicationConfiguration;
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
@DirtiesContext
@ActiveProfiles(ApplicationConfig.ConfigProfile.INTEGRATION_TEST)
@Sql(scripts = "/dml-login.sql")
public abstract class BaseIntegrationTestConfig {
}
