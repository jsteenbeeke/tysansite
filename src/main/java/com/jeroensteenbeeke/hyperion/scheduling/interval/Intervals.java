/*
 * Copyright 2010-2011 Jeroen Steenbeeke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeroensteenbeeke.hyperion.scheduling.interval;

public final class Intervals {
	private Intervals() {
	}

	public static Interval seconds(int seconds) {
		if (seconds < 1 || seconds > 59)
			throw new IllegalArgumentException(
					"seconds should be at least 1 and at most 59");

		return new DefaultInterval("*/" + seconds + " * * * * ?");
	}

	public static Interval minutes(int minutes) {
		if (minutes < 1 || minutes > 59)
			throw new IllegalArgumentException(
					"minutes should be at least 1 and at most 59");

		return new DefaultInterval("0 */" + minutes + " * * * ?");
	}

	public static Interval hours(int hours) {
		if (hours < 1 || hours > 24)
			throw new IllegalArgumentException(
					"hours should be at least 1 and at most 24");

		return new DefaultInterval("0 0 */" + hours + " * * ?");
	}

	public static Interval days(int days) {
		if (days < 1)
			throw new IllegalArgumentException("days should be at least 1");

		return new DefaultInterval("0 0 6 */" + days + " * ?");
	}
}
