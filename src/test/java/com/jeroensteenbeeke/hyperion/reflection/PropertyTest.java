/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
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
package com.jeroensteenbeeke.hyperion.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.junit.Test;

public class PropertyTest {
	private String field;

	@SuppressWarnings("unchecked")
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		Property<PropertyTest> prop = Reflector.getProperty(PropertyTest.class,
				"field");

		assertNotNull(prop.getter());
		assertNotNull(prop.setter());
		assertNotNull(prop.field());

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(buffer);

		oos.writeObject(prop);

		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				buffer.toByteArray()));
		Property<PropertyTest> prop2 = (Property<PropertyTest>) ois
				.readObject();

		assertEquals(prop.getter(), prop2.getter());
		assertEquals(prop.setter(), prop2.setter());
		assertEquals(prop.field(), prop2.field());
		assertEquals(prop.name(), prop2.name());
		assertEquals(prop.owningClass(), prop2.owningClass());

		oos.close();

	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
