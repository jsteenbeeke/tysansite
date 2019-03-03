package com.tysanclan.site.projectewok.components;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.TypedPanel;
import com.tysanclan.site.projectewok.beans.HumorService;
import com.tysanclan.site.projectewok.entities.DisneyCharacter;
import com.tysanclan.site.projectewok.entities.User;
import io.vavr.control.Option;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Random;

public class DisneyHuntPanel extends TypedPanel<User> {
	private static final long serialVersionUID = 1L;

	private static final Random random = new Random();

	private static final int RARITY = 3;

	private final boolean shouldShow;

	@SpringBean
	private HumorService humorService;

	public DisneyHuntPanel(String id, User user) {
		super(id, ModelMaker.wrap(user));

		Option<DisneyCharacter> c = humorService
				.findDisneyCharacter(user, random);

		shouldShow = isAprilFoolsDay2019() && c.isDefined()
				&& random.nextInt(RARITY) == 0;

		// Default to Mickey Mouse in case of no characters available
		final DisneyCharacter character = c.getOrElse(DisneyCharacter.MICKEY);

		add(new AjaxLink<DisneyCharacter>("collect") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (DisneyHuntPanel.this.isVisibilityAllowed()) {
					humorService.catchDisneyCharacter(
							DisneyHuntPanel.this.getModelObject(), character);
					DisneyHuntPanel.this.setVisible(false);
					target.add(DisneyHuntPanel.this);
				}
			}
		});

		ContextImage image = new ContextImage("character",
				"images/2019-04-01/" + character.getUrl());
		image.add(AttributeModifier.append("style", "max-height: 200px; height: 200px;"));
		add(image);


		setOutputMarkupPlaceholderTag(true);
		add(new AbstractAjaxTimerBehavior(Duration.seconds(10)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTimer(AjaxRequestTarget target) {
				DisneyHuntPanel.this.setVisible(false);

				target.add(DisneyHuntPanel.this);

				stop(target);
			}
		});
	}

	public boolean isAprilFoolsDay2019() {
		final DateTime easternStandardTime = new DateTime(
				DateTimeZone.forID("EST"));

		return easternStandardTime.getDayOfMonth() == 1
				&& easternStandardTime.getMonthOfYear() == 4
				&& easternStandardTime.getYear() == 2019;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		setVisibilityAllowed(shouldShow);
	}
}
