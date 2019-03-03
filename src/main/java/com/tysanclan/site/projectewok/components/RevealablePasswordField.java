package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;

public class RevealablePasswordField extends PasswordTextField {

	private static final long serialVersionUID = 1L;

	public RevealablePasswordField(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	public RevealablePasswordField(String id, IModel<String> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(JavaScriptHeaderItem
				.forReference(RevealablePasswordFieldResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(
				String.format("$('#%s').revealable();", getMarkupId())));
	}
}
