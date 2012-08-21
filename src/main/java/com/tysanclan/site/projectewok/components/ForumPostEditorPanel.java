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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.spring.injection.annot.SpringBean;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.tysanclan.site.projectewok.entities.dao.MobileUserAgentDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.MobileUserAgentFilter;
import com.tysanclan.site.projectewok.util.HTMLSanitizer;

/**
 * A panel to edit a forum post
 * 
 * @author Jeroen Steenbeeke
 */
public class ForumPostEditorPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private TextArea<String> editor;

	@SpringBean
	private MobileUserAgentDAO mobileUserAgentDAO;

	private final boolean mobile;

	public ForumPostEditorPanel(String id, String content) {
		super(id);

		WebClientInfo info = (WebClientInfo) RequestCycle.get().getClientInfo();
		mobile = isMobile(info.getUserAgent());

		editor = new TextArea<String>("postcontent", new Model<String>(
				mobile ? HTMLSanitizer.paragraphsToNewlines(content) : content));

		if (!mobile) {
			editor.add(new TinyMceBehavior(new TysanTinyMCESettings()));
		}

		add(editor);
	}

	private boolean isMobile(String userAgent) {
		MobileUserAgentFilter filter = new MobileUserAgentFilter();
		MobileUserAgentFilter filter2 = new MobileUserAgentFilter();

		filter.setIdentifier(userAgent);
		filter.setMobile(true);

		filter2.setIdentifier(userAgent);
		filter2.setMobile(false);

		return mobileUserAgentDAO.countByFilter(filter) > 0
				&& mobileUserAgentDAO.countByFilter(filter2) == 0;
	}

	public String getEditorContent() {
		String content = editor.getModelObject();
		if (content == null)
			content = "";

		if (!mobile) {
			return content;
		}
		return HTMLSanitizer.newlinesToParagraphs(content);

	}
}
