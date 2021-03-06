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
package com.tysanclan.site.projectewok.pages.member.senate;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.NaiveRenderer;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.BBCodeTextArea;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.RegulationChange;
import com.tysanclan.site.projectewok.entities.dao.RegulationDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.SENATOR, Rank.CHANCELLOR })
public class ModifyRegulationPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RegulationDAO regulationDAO;

	/**
	 *
	 */
	public ModifyRegulationPage() {
		super("Modify Regulation");

		add(createModifyForm());

	}

	/**
	 */
	private Form<RegulationChange> createModifyForm() {
		Form<RegulationChange> form = new Form<RegulationChange>("editForm") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private DemocracyService democracyService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextArea<String> descriptionArea = (TextArea<String>) get(
						"description");
				TextField<String> newTitleField = (TextField<String>) get(
						"newTitle");
				DropDownChoice<Regulation> regulationChoice = (DropDownChoice<Regulation>) get(
						"regulation");

				String newDescription = descriptionArea.getModelObject();
				String newTitle = newTitleField.getModelObject();
				Regulation regulation = regulationChoice.getModelObject();

				RegulationChange vote = democracyService
						.createModifyRegulationVote(getUser(), regulation,
								newTitle, newDescription);
				if (vote != null) {
					if (getUser().getRank() == Rank.SENATOR) {
						setResponsePage(new RegulationModificationPage());
					} else {
						setResponsePage(new VetoPage());
					}
				}

			}

		};

		form.add(new TextField<String>("newTitle", Model.of("")));

		form.add(new BBCodeTextArea("description", "").setRequired(true));

		form.add(new Label("example", Model.of(""))
				.setEscapeModelStrings(false).setVisible(false)
				.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		form.add(new DropDownChoice<Regulation>("regulation",
				ModelMaker.wrap(Regulation.class),
				ModelMaker.wrapList(regulationDAO.findAll().toJavaList()),
									  new NaiveRenderer<Regulation>() {
										  private static final long serialVersionUID = 1L;

										  /**
										   * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
										   */
										  @Override
										  public Object getDisplayValue(Regulation object) {
											  return object.getName();
										  }

										  /**
										   * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object,
										   * int)
										   */
										  @Override
										  public String getIdValue(Regulation object, int index) {
											  return object.getId().toString();
										  }
									  }).setNullValid(false)
				.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						Form<Regulation> regForm = (Form<Regulation>) getComponent()
								.getParent();
						Label example = (Label) regForm.get("example");

						DropDownChoice<Regulation> regulationChoice = (DropDownChoice<Regulation>) regForm
								.get("regulation");
						Regulation regulation = regulationChoice
								.getModelObject();

						if (regulation != null) {

							Component example2 = new Label("example",
									new Model<String>(regulation.getContents()))
									.setEscapeModelStrings(false)
									.setVisible(true).setOutputMarkupId(true)
									.setOutputMarkupPlaceholderTag(true);

							example.replaceWith(example2);

							if (target != null) {
								target.add(example2);
							}
						} else {
							example.setVisible(false);
							if (target != null) {
								target.add(example);
							}
						}
					}

				}));

		return form;
	}
}
