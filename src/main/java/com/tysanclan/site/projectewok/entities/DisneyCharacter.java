package com.tysanclan.site.projectewok.entities;

public enum DisneyCharacter {
	MICKEY("Mickey_Mouse.png", 400), PLUTO("Plutodog.gif", 200), GOOFY(
			"Goofy.png", 200), ELSA("Elsa.png", 20), MAUI("Maui.png",
			40), MERIDA("Merida.png", 20), MUFASA("Mufasa.png", 10), STITCH(
			"Stitch.png", 5);

	private final String url;

	private final int prevalence;

	DisneyCharacter(String url, int prevalence) {
		this.url = url;
		this.prevalence = prevalence;
	}

	public String getUrl() {
		return url;
	}

	public int getPrevalence() {
		return prevalence;
	}
}
