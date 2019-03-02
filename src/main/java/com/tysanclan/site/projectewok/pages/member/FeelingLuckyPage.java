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
package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.entities.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Random;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class FeelingLuckyPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	private static final Random rand = new Random();

	/**
	 *
	 */
	public FeelingLuckyPage() {
		super("Feeling lucky");

		add(new Label("score", new Model<>(getUser().getLuckyScore())));

		add(new Form<User>("luckyForm") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private MembershipService membershipService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				int score = rand.nextInt(90);

				membershipService.feelLucky(getUser());

				if (score < 4) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=h1ZAXYyxd08");
				} else if (score < 8) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=WQgLNUq0ktI");
				} else if (score < 12) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=3KANI2dpXLw");
				} else if (score < 16) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=J_DV9b0x7v4");
				} else if (score < 20) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=a-79sbicwTQ");
				} else if (score < 24) {
					setResponsePage(new PastElectionsPage());
				} else if (score < 28) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=Mo8Qls0HnWo");
				} else if (score < 32) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=nr_CJL1YQRc");
				} else if (score < 36) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=uIg9VaMBi9o");
				} else if (score < 40) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=QZtfphBDp-w");
				} else if (score < 44) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=dcXNXKtu8z4");
				} else if (score < 48) {
					throw new RedirectToUrlException(
							"http://www.youtube.com/watch?v=CIZYqZrdwIM");
				} else {
					setResponsePage(new OverviewPage());
				}
			}
		});
	}
}
