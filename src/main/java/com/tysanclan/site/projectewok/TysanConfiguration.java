package com.tysanclan.site.projectewok;

import com.google.common.base.Strings;
import com.jeroensteenbeeke.hyperion.events.DefaultEventDispatcher;
import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.jeroensteenbeeke.hyperion.rollbar.IRollBarDeployNotifier;
import com.jeroensteenbeeke.hyperion.rollbar.NoopRollBarDeployNotifier;
import com.jeroensteenbeeke.hyperion.rollbar.RollBarDeployNotifier;
import com.jeroensteenbeeke.hyperion.rollbar.RollBarReference;
import com.jeroensteenbeeke.hyperion.solstice.spring.db.EnableSolstice;
import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.ConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.tysanclan.site.projectewok.beans.impl",
		"com.tysanclan.site.projectewok.entities.dao.hibernate",
		"com.tysanclan.site.projectewok.rs.services",
		"com.tysanclan.site.projectewok.rs.helpers"}, scopedProxy = ScopedProxyMode.INTERFACES)
@EnableTransactionManagement
@EnableSolstice(entityBasePackage = "com.tysanclan.site.projectewok.entities", liquibaseChangelog = "classpath:/com/tysanclan/site/projectewok/entities/liquibase/db.changelog-master.xml")
@PropertySource("file:${hyperion.configdir:${user.home}/.hyperion}/ewok.properties")
public class TysanConfiguration {

	@Bean
	public IEventDispatcher eventDispatcher() {
		return new DefaultEventDispatcher();
	}

	@Bean
	public JavaMailSender mailSender(@Value("mail.server") String mailServer,
									 @Value("mail.username") String mailUser,
									 @Value("mail.password") String mailPassword) {
		Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(mailServer);
		sender.setUsername(mailUser);
		sender.setPassword(mailPassword);
		sender.setPort(587);
		sender.setJavaMailProperties(javaMailProperties);

		return sender;

	}

	@Bean
	@Conditional(RollbarCondition.class)
	@Scope("singleton")
	public Rollbar rollbar(@Value("${rollbar.apiKey:}") String apiKey, @Value("${rollbar.environment:}") String environment) {
		Rollbar rollbar = Rollbar.init(ConfigBuilder.withAccessToken(apiKey).environment(environment).build());

		RollBarReference.instance.excludeException("org.eclipse.jetty.io.EofException");
		RollBarReference.instance.setRollbar(rollbar);

		return rollbar;
	}

	@Bean
	@Scope("singleton")
	public IRollBarDeployNotifier deployNotifier(@Value("${rollbar.apiKey:}") String apiKey, @Value("${rollbar.environment:}") String environment, @Value("${rollbar.deployingUser:}") String deployingUser) {
		if (Strings.isNullOrEmpty(apiKey) || Strings.isNullOrEmpty(environment) || Strings.isNullOrEmpty(deployingUser)) {
			return new NoopRollBarDeployNotifier();
		}

		return RollBarDeployNotifier.createNotifier().withApiKey(apiKey)
									.withEnvironment(environment)
									.andDeployingUser(deployingUser);
	}
}
