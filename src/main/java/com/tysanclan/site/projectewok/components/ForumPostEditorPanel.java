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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * A panel to edit a forum post
 *
 * @author Jeroen Steenbeeke
 */
public class ForumPostEditorPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private TextArea<String> editor;

	public ForumPostEditorPanel(String id, String content) {
		super(id);

		editor = new BBCodeTextArea("postcontent", content);

		add(editor);
	}

	public String getEditorContent() {
		String content = editor.getModelObject();
		if (content == null)
			content = "";

		return content;

	}
}
