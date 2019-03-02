package com.tysanclan.site.projectewok;

import com.jeroensteenbeeke.hyperion.events.DefaultEventDispatcher;
import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.jeroensteenbeeke.hyperion.solstice.spring.db.EnableSolstice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = { "com.tysanclan.site.projectewok.beans.impl",
		"com.tysanclan.site.projectewok.entities.dao.hibernate",
		"com.tysanclan.site.projectewok.rs.services",
		"com.tysanclan.site.projectewok.rs.helpers" }, scopedProxy = ScopedProxyMode.INTERFACES)
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

}
