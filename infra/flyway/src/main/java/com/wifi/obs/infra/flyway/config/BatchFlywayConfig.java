package com.wifi.obs.infra.flyway.config;

import static com.wifi.obs.infra.batch.config.BatchDataSourceConfig.DATASOURCE_NAME;

import com.wifi.obs.infra.batch.config.BatchDataSourceConfig;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({BatchDataSourceConfig.class})
public class BatchFlywayConfig {
	private static final String SERVICE_NAME = "wifiobs";
	private static final String MODULE_NAME = "infra";

	// base property prefix for flyway
	public static final String BASE_PROPERTY_PREFIX =
			SERVICE_NAME + "." + MODULE_NAME + ".flyway.batch";

	// bean name for flyway configuration
	private static final String BEAN_PROPERTY_PREFIX = "wifiobsbatch";
	private static final String FLYWAY = BEAN_PROPERTY_PREFIX + "Flyway";
	private static final String FLYWAY_PROPERTIES = BEAN_PROPERTY_PREFIX + "FlywayProperties";
	private static final String FLYWAY_MIGRATION_INITIALIZER =
			BEAN_PROPERTY_PREFIX + "FlywayMigrationInitializer";
	private static final String FLYWAY_VALIDATE_INITIALIZER =
			BEAN_PROPERTY_PREFIX + "FlywayValidateInitializer";
	private static final String FLYWAY_CONFIGURATION = BEAN_PROPERTY_PREFIX + "FlywayConfiguration";

	@Bean(name = FLYWAY)
	public Flyway flyway(
			@Qualifier(value = FLYWAY_CONFIGURATION)
					org.flywaydb.core.api.configuration.Configuration configuration) {
		return new Flyway(configuration);
	}

	@Profile({"!local"})
	@Bean(name = FLYWAY_VALIDATE_INITIALIZER)
	public FlywayMigrationInitializer flywayValidateInitializer(
			@Qualifier(value = FLYWAY) Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::validate);
	}

	@Profile({"!local"})
	@Bean(name = FLYWAY_MIGRATION_INITIALIZER)
	public FlywayMigrationInitializer flywayMigrationInitializer(
			@Qualifier(value = FLYWAY) Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::migrate);
	}

	@Bean(name = FLYWAY_PROPERTIES)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX)
	public FlywayProperties flywayProperties() {
		return new FlywayProperties();
	}

	@Bean(name = FLYWAY_CONFIGURATION)
	public org.flywaydb.core.api.configuration.Configuration configuration(
			@Qualifier(value = FLYWAY_PROPERTIES) FlywayProperties flywayProperties,
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource) {

		FluentConfiguration configuration = new FluentConfiguration();
		configuration.dataSource(dataSource);
		flywayProperties.getLocations().forEach(configuration::locations);
		return configuration;
	}
}
