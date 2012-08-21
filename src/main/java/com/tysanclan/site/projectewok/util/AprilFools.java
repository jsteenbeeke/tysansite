/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.util;

import java.util.Random;

import org.apache.wicket.RedirectToUrlException;

/**
 * @author jeroen
 */
public final class AprilFools {

	private static final String[] animals = { "images/animals/scorpion.jpeg",
			"images/animals/tasmanian_devil.jpeg",
			"images/animals/scalzicats.jpg", "images/animals/wolf.jpeg",
			"images/animals/shark.jpeg", "images/animals/kitten.jpeg",
			"images/animals/seal.jpeg", "images/animals/seaotter.jpeg",
			"images/animals/platypus.jpeg", "images/animals/lion.jpeg" };

	private static final String[] animal_options = { "Feed it", "Eat it",
			"Kill it", "Run away!" };

	private static final String[] videos = {
			"http://www.youtube.com/watch?v=h1ZAXYyxd08",
			"http://www.youtube.com/watch?v=WQgLNUq0ktI",
			"http://www.youtube.com/watch?v=3KANI2dpXLw",
			"http://www.youtube.com/watch?v=J_DV9b0x7v4",
			"http://www.youtube.com/watch?v=a-79sbicwTQ",
			"http://www.youtube.com/watch?v=Mo8Qls0HnWo",
			"http://www.youtube.com/watch?v=nr_CJL1YQRc",
			"http://www.youtube.com/watch?v=uIg9VaMBi9o",
			"http://www.youtube.com/watch?v=QZtfphBDp-w",
			"http://www.youtube.com/watch?v=dcXNXKtu8z4",
			"http://www.youtube.com/watch?v=JBrcA8Dgl60",
			"http://www.youtube.com/watch?v=pi00ykRg_5c",
			"http://www.youtube.com/watch?v=Xvl3qJe9L9g",
			"http://www.youtube.com/watch?v=4pXfHLUlZf4",
			"http://www.youtube.com/watch?v=NisCkxU544c",
			"http://www.youtube.com/watch?v=lQlIhraqL7o",
			"http://www.youtube.com/watch?v=H4lb33CNi7g",
			"http://www.weebls-stuff.com/songs/Narwhals/",
			"http://www.weebls-stuff.com/songs/Baby+Baboon/",
			"http://www.weebls-stuff.com/songs/Flying+Squirrels/",
			"http://www.weebls-stuff.com/songs/Hedgehogs/"

	};

	public static final String KEY_ANIMALS = "how.to.handle.animals";

	public static final Random rand = new Random();

	private AprilFools() {
	}

	public static String getRandomAnimalOption() {
		int index = rand.nextInt(animal_options.length);

		return animal_options[index];
	}

	public static String getRandomAnimal() {
		int index = rand.nextInt(animals.length);

		return animals[index];
	}

	public static void performRickRoll() {
		int index = rand.nextInt(videos.length);

		throw new RedirectToUrlException(videos[index]);
	}

	public static String[] getOptions() {
		return animal_options;
	}

}
