package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBParseException;

public class BBCodePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public BBCodePanel(String id, String text) {
		this(id, Model.of(text));
	}

	public BBCodePanel(String id, IModel<String> text) {
		super(id);

		add(new Label("text", new BBCodeModel(text)).setEscapeModelStrings(
				false).setRenderBodyOnly(true));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(getCssHeaderItem());
		response.render(JavaScriptHeaderItem
				.forReference(BBCodePanelInitScriptResource.get()));
	}

	public CssHeaderItem getCssHeaderItem() {
		return CssHeaderItem.forUrl("css/bbcode.css");
	}

	private static class BBCodeModel implements IModel<String> {
		private static final long serialVersionUID = 1L;

		private IModel<String> delegate;

		public BBCodeModel(IModel<String> delegate) {
			this.delegate = delegate;
		}

		@Override
		public String getObject() {
			String original = delegate.getObject();

			try {
				return BBCodeUtil.toHtml(original);
			} catch (BBParseException e) {
				// Should not occur when input is done using BBCodeTextArea
				return original;
			}
		}

		@Override
		public void setObject(String object) {
			delegate.setObject(object);
		}

		@Override
		public void detach() {
			delegate.detach();

		}
	}

}
