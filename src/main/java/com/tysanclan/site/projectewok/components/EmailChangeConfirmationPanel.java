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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.EmailChangeConfirmation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.EmailChangeConfirmationDAO;
import com.tysanclan.site.projectewok.entities.filter.EmailChangeConfirmationFilter;
import io.vavr.collection.Seq;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class EmailChangeConfirmationPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private EmailChangeConfirmationDAO emailChangeConfirmationDAO;

	public EmailChangeConfirmationPanel(String id, User user) {
		super(id);

		EmailChangeConfirmationFilter filter = new EmailChangeConfirmationFilter();
		filter.user(user);
		filter.id().orderBy(true);

		Seq<EmailChangeConfirmation> confirmations = emailChangeConfirmationDAO
				.findByFilter(filter);

		EmailChangeConfirmation confirmation = null;

		if (!confirmations.isEmpty()) {
			confirmation = confirmations.get(0);
		} else {
			setVisible(false);
		}

		Form<EmailChangeConfirmation> confirmationForm = new Form<EmailChangeConfirmation>(
				"confirmForm", ModelMaker.wrap(confirmation)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			@SpringBean
			private EmailChangeConfirmationDAO _emailChangeConfirmationDAO;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				EmailChangeConfirmation conf = getModelObject();

				TextField<String> codeField = (TextField<String>) get("code");
				String code = codeField.getModelObject();

				if (code.equals(conf.getActivationKey())) {

					userService.setUserMail(conf.getUser(), conf.getEmail());
					_emailChangeConfirmationDAO.delete(conf);
					onConfirmed();

				} else {
					error("Confirmation code incorrect");
				}
			}

		};

		add(confirmationForm);

		confirmationForm.add(new Label("mail",
				confirmation != null ? confirmation.getEmail() : ""));
		confirmationForm
				.add(new TextField<String>("code", new Model<String>("")));
		confirmationForm.add(new AjaxLink<EmailChangeConfirmation>("cancel",
				ModelMaker.wrap(confirmation)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private EmailChangeConfirmationDAO _emailChangeConfirmationDAO;

			/**
			 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
			 */
			@Override
			public void onClick(AjaxRequestTarget target) {
				EmailChangeConfirmation conf = (EmailChangeConfirmation) getModelObject();
				_emailChangeConfirmationDAO.delete(conf);
				onCancel();
			}
		});
	}

	public abstract void onConfirmed();

	public abstract void onCancel();

}
