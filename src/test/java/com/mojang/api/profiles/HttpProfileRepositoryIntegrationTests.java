package com.mojang.api.profiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.vavr.collection.Array;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HttpProfileRepositoryIntegrationTests {

	@Test
	public void findProfilesByNames_existingNameProvided_returnsProfile()
			throws Exception {
		ProfileRepository repository = new HttpProfileRepository("minecraft");

		Profile[] profiles = repository.findProfilesByNames("mollstam");

		assertThat(profiles.length, is(1));
		assertThat(profiles[0].getName(), is(equalTo("mollstam")));
		assertThat(profiles[0].getId(),
				is(equalTo("f8cdb6839e9043eea81939f85d9c5d69")));
	}

	@Test
	public void findProfilesByNames_existingMultipleNamesProvided_returnsProfiles()
			throws Exception {
		ProfileRepository repository = new HttpProfileRepository("minecraft");

		Profile[] profiles = repository.findProfilesByNames("mollstam",
				"KrisJelbring");

		assertEquals(Array.of("KrisJelbring", "mollstam"), Array.of(profiles).map(Profile::getName).sorted());
		assertEquals(Array.of("7125ba8b1c864508b92bb5c042ccfe2b", "f8cdb6839e9043eea81939f85d9c5d69"), Array.of(profiles).map(Profile::getId).sorted());
	}

	@Test
	public void findProfilesByNames_nonExistingNameProvided_returnsEmptyArray()
			throws Exception {
		ProfileRepository repository = new HttpProfileRepository("minecraft");

		Profile[] profiles = repository
				.findProfilesByNames("doesnotexist$*not even legal");

		assertThat(profiles.length, is(0));
	}
}
