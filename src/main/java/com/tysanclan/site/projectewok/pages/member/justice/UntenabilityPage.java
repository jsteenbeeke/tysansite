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
package com.tysanclan.site.projectewok.pages.member.justice;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.NaiveRenderer;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.UntenabilityVote;
import com.tysanclan.site.projectewok.entities.dao.RegulationDAO;
import com.tysanclan.site.projectewok.entities.dao.UntenabilityVoteDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.TRUTHSAYER)
public class UntenabilityPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RegulationDAO regulationDAO;

	@SpringBean
	private UntenabilityVoteDAO untenabilityVoteDAO;

	/**
	 *
	 */
	public UntenabilityPage() {
		super("Untenability Vote");

		Form<?> initiateForm = new Form<Void>("initiateForm") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private DemocracyService democracyService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<Regulation> regulationChoice = (DropDownChoice<Regulation>) get(
						"regulation");
				Regulation regulation = regulationChoice.getModelObject();

				democracyService.createUntenabilityVote(getUser(), regulation);

				setResponsePage(new UntenabilityPage());

			}
		};

		List<Regulation> regulations = regulationDAO.findAll().toJavaList();

		for (UntenabilityVote vote : untenabilityVoteDAO.findAll()) {
			regulations.remove(vote.getRegulation());
		}

		IModel<Regulation> choiceModel = regulations.isEmpty() ? ModelMaker.wrap(Regulation.class) : ModelMaker.wrap(regulations.get(0));

		initiateForm.add(new DropDownChoice<Regulation>("regulation", choiceModel,
											  ModelMaker.wrapList(regulations),
											  new NaiveRenderer<Regulation>() {
												  private static final long serialVersionUID = 1L;

												  /**
												   * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
												   */
												  @Override
												  public Object getDisplayValue(Regulation object) {
													  return object.getName();
												  }

												  @Override
												  public String getIdValue(Regulation object, int index) {
													  return object.getId().toString();
												  }
											  }));

		add(initiateForm);

	}
}
