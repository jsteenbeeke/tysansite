package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;

public class BBCodeTextArea extends TextArea<String> {

	private static final long serialVersionUID = 1L;

	public BBCodeTextArea(String id, String text) {
		this(id, new BBCodeFilterModel(text));
	}

	public BBCodeTextArea(String id, BBCodeFilterModel model) {
		super(id, model);

		init();
	}

	private void init() {
		add(new BBCodeValidator());
		setOutputMarkupId(true);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(CssHeaderItem.forUrl("css/bbcode.css"));
		response.render(JavaScriptHeaderItem
				.forReference(BBCodeTextAreaPluginResource.get()));
		response.render(OnDomReadyHeaderItem.forScript(String.format(
				"$('#%s').bbedit();", getMarkupId())));
	}

	public static class BBCodeFilterModel implements IModel<String> {
		private static final long serialVersionUID = 1L;

		private String data;

		public BBCodeFilterModel(String data) {
			super();
			this.data = BBCodeUtil.stripTags(data);
		}

		@Override
		public String getObject() {
			return data;
		}

		@Override
		public void setObject(String object) {
			this.data = BBCodeUtil.stripTags(object);
		}

		@Override
		public void detach() {

		}
	}

}
