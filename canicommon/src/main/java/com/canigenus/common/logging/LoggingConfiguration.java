package com.canigenus.common.logging;

import java.io.Serializable;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.zip.Deflater;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class LoggingConfiguration {
	public static final String PATTERN_LAYOUT = "[%d] [%t] [%-5level] - %msg (%logger{1}:%L) %n%throwable";
	public static final String LOG_FILE_NAME = "app.log";
	public static final String LOG_FILE_NAME_PATTERN = LOG_FILE_NAME
			+ "-yyyy.MM.dd";

	static {
		ConfigurationFactory
				.setConfigurationFactory(new Log4j2ConfigurationFactory());
	}

	/**
	 * Just to make JVM visit this class to initialize the static parts.
	 */
	public static void configure() {
	}

	@Plugin(category = "ConfigurationFactory", name = "Log4j2ConfigurationFactory")
	@Order(0)
	public static class Log4j2ConfigurationFactory extends ConfigurationFactory {

		@Override
		protected String[] getSupportedTypes() {
			return null;
		}

		@Override
		public Configuration getConfiguration(ConfigurationSource source) {
			return new Log4j2Configuration();
		}

		@Override
		public Configuration getConfiguration(String name, URI configLocation) {
			return new Log4j2Configuration();
		}

	}

	private static class Log4j2Configuration extends DefaultConfiguration {

		public Log4j2Configuration() {
			setName("app-log4j2");
			String root = System.getProperty("APP_ROOT", "/tmp");
			if (!root.endsWith("/")) {
				root += "/";
			}
			// MARKER
			Layout<? extends Serializable> layout = PatternLayout.createLayout(
					PATTERN_LAYOUT, null, null, null, true, true, null, null);

			String oneDay = TimeUnit.DAYS.toMillis(1) + "";
			String oneMB = (1024 * 1024) + "";
			final TimeBasedTriggeringPolicy timeBasedTriggeringPolicy = TimeBasedTriggeringPolicy
					.createPolicy(oneDay, "true");
			final SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy = SizeBasedTriggeringPolicy
					.createPolicy(oneMB);
			final CompositeTriggeringPolicy policy = CompositeTriggeringPolicy
					.createPolicy(timeBasedTriggeringPolicy,
							sizeBasedTriggeringPolicy);
			final DefaultRolloverStrategy strategy = DefaultRolloverStrategy
					.createStrategy("7", "1", null,
							Deflater.DEFAULT_COMPRESSION + "", this);
			Appender appender = RollingFileAppender.createAppender(root
					+ LOG_FILE_NAME, LOG_FILE_NAME_PATTERN, "true",
					"app-log-file-appender", "true", "true",null, policy, strategy,
					layout, null, null, null, null, null);
			addAppender(appender);
			getRootLogger().addAppender(appender, Level.INFO, null);
		}
	}
}