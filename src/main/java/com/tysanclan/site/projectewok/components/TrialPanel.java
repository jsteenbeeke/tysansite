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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.Trial.Verdict;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class TrialPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TrialPanel(String id, Trial trial, User user) {
		super(id);

		if (MemberUtil.isMember(trial.getAccused())) {
			add(new MemberListItem("accused", trial.getAccused()));
		} else {
			add(new Label("accused", trial.getAccused().getUsername()));
		}

		add(new ListView<Regulation>("regulations", ModelMaker.wrap(trial
				.getRegulations())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Regulation> item) {
				Regulation regulation = item.getModelObject();
				item.add(new Label("name", regulation.getName()));

			}

		});

		if (MemberUtil.isMember(trial.getAccused())) {
			add(new MemberListItem("truthsayer", trial.getJudge()));
		} else {
			add(new Label("truthsayer", trial.getJudge().getUsername()));
		}

		add(new BBCodePanel("evidence", trial.getMotivation()).setVisible(trial
				.getJudge().equals(user)));

		ForumThread thread = trial.getTrialThread();

		Calendar cal = DateUtil.getMidnightCalendarInstance();
		Calendar cal2 = Calendar.getInstance();

		cal2.setTime(thread.getPostTime());

		cal2.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.MONTH, cal2.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, cal2.get(Calendar.YEAR));

		boolean canVerdictBePassed = trial.getAccused().getRank() == Rank.TRIAL
				|| new Date().after(cal.getTime());
		boolean hasVerdict = trial.getVerdict() != null;
		boolean isJudge = user != null && user.equals(trial.getJudge());

		Form<Trial> verdictForm = new Form<Trial>("verdictForm",
				ModelMaker.wrap(trial)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private LawEnforcementService lawEnforcementService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				Trial t = getModelObject();

				DropDownChoice<Verdict> verdictChoice = (DropDownChoice<Verdict>) get("verdict");

				Verdict verdict = verdictChoice.getModelObject();

				lawEnforcementService.passVerdict(t, verdict);

				setResponsePage(new ForumThreadPage(t.getTrialThread().getId(),
						1, false));
			}

		};

		verdictForm.add(new DropDownChoice<Verdict>("verdict",
				new Model<Verdict>(Verdict.INNOCENT), Arrays.asList(Verdict
						.values()), new IChoiceRenderer<Verdict>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object getDisplayValue(Verdict object) {
						switch (object) {
							case INNOCENT:
								return "Member is innocent";
							case MINOR:
								return "Member is guilty, but will only be given a warning";
							case MEDIUM:
								return "Member is guilty, and will be reprimanded";
							case MAJOR:
								return "Member is guilty and will be banned immediately";
							default:
								return "I have no idea";
						}

					}

					@Override
					public String getIdValue(Verdict object, int index) {
						return object.toString();
					}
				}));

		verdictForm.setVisible(canVerdictBePassed && !hasVerdict && isJudge);

		add(new IconLink.Builder(
				trial.isRestrained() ? "images/icons/lock_delete.png"
						: "images/icons/lock.png",
				new DefaultClickResponder<Trial>(ModelMaker.wrap(trial)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private LawEnforcementService lawEnforcementService;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						Trial t = getModelObject();

						if (t.isRestrained()) {
							lawEnforcementService.unrestrainAccused(t);
						} else {
							lawEnforcementService.restrainAccused(t);
						}

						setResponsePage(new ForumThreadPage(t.getTrialThread()
								.getId(), 1, false));
					}

				})
				.setText(
						trial.isRestrained() ? "Lift restraint"
								: "Restrain the accused")
				.newInstance("restrain").setVisible(isJudge));

		if (hasVerdict) {
			switch (trial.getVerdict()) {
				case MAJOR:
					add(new Label("verdict",
							"Member was found guilty, and removed from the clan"));
					break;
				case MEDIUM:
					add(new Label("verdict",
							"Member was found guilty, and given a reprimand"));
					break;
				case MINOR:
					add(new Label("verdict",
							"Member was found guilty, and given a warning"));
					break;
				case INNOCENT:
					add(new Label("verdict", "Member was found innocent"));
					break;
				default:
					add(new WebMarkupContainer("verdict").setVisible(false));
			}
		} else {
			add(new WebMarkupContainer("verdict").setVisible(false));
		}

		add(verdictForm);
	}
}
