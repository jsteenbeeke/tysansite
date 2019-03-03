package com.tysanclan.site.projectewok.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilTest {
	@Test
	public void testDigitMath() {
		testRange(1, 0, 10);
		testRange(2, 10, 100);
		testRange(3, 100, 1000);
		testRange(4, 1000, 10000);
		testRange(5, 10000, 100000);
		testRange(6, 100000, 1000000);
		testRange(7, 1000000, 10000000);

		testRange(3, -99, -9);
		testRange(4, -999, -99);
		testRange(5, -9999, -999);
		testRange(6, -99999, -9999);
		testRange(7, -999999, -99999);

	}

	private void testRange(final int digits, final int low, final int highEx) {
		for (int i = low; i < highEx; i++) {
			assertEquals(
					String.format("Number of digits in %d is %d", i, digits),
					digits, MathUtil.countPrintableDigits(i));
		}
	}
}
