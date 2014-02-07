package com.tysanclan.site.projectewok.util;

public final class MathUtil {
	private MathUtil() {

	}

	public static int countPrintableDigits(int input) {
		if (input == 0)
			return 1;

		int pinput = Math.abs(input);
		int extra = input < pinput ? 1 : 0;

		return (int) Math.ceil(Math.log10(pinput + 1)) + extra;
	}
}
