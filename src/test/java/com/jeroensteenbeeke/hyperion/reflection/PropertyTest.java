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
