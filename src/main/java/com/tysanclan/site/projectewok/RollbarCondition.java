package com.tysanclan.site.projectewok;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Set;

public class RollbarCondition implements Condition {
	private static final Set<String> REQUIRED_ROLLBAR_PROPERTIES = ImmutableSet.<String>builder()
			.add("rollbar.apiKey")
			.add("rollbar.environment")
			.add("rollbar.deployingUser").build();

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return REQUIRED_ROLLBAR_PROPERTIES
				.stream()
				.noneMatch(key -> Strings.isNullOrEmpty(context.getEnvironment().getProperty(key)));
	}
}
