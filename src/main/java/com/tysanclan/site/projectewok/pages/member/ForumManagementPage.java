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
package com.tysanclan.site.projectewok.pages.member;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.GroupForum;
import com.tysanclan.site.projectewok.entities.NewsForum;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.dao.ForumCategoryDAO;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class ForumManagementPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumCategoryDAO forumCategoryDAO;

	/**
	 * 
	 */
	public ForumManagementPage() {
		super("Forum Management");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		Form<ForumCategory> addForm = createAddCategoryForm();
		accordion.add(createCategoryListView());

		accordion.add(addForm);

		add(accordion);
	}

	/**
	 	 */
	private ListView<ForumCategory> createCategoryListView() {
		return new ListView<ForumCategory>("categories",
				ModelMaker.wrap(forumCategoryDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ForumCategory> item) {
				final int catindex = item.getIndex();

				ForumCategory category = item.getModelObject();
				item.add(new Label("name", category.getName()));

				Link<ForumCategory> createForumLink = new Link<ForumCategory>(
						"add", ModelMaker.wrap(category)) {

					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new CreateForumPage(getModelObject()));
					}

				};

				createForumLink.add(new ContextImage("icon",
						"images/icons/book_add.png"));

				item.add(createForumLink);

				Link<ForumCategory> deleteLink = new Link<ForumCategory>(
						"delete", ModelMaker.wrap(category)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private ForumService forumService;

					@Override
					public void onClick() {
						forumService
								.deleteCategory(getUser(), getModelObject());
						setResponsePage(new ForumManagementPage());
					}

				};
				deleteLink.setVisible(category.getForums().isEmpty());
				deleteLink.add(new ContextImage("icon",
						"images/icons/cross.png"));
				deleteLink.add(new Label("name2", category.getName()));
				item.add(deleteLink);
				item.add(new Label(
						"allowpublic",
						category.isAllowPublicGroupForums() ? "This category may contain public group forums"
								: "This category may not contains public group forums"));

				final int total = category.getForums().size();

				item.add(new ListView<Forum>("forums", ModelMaker.wrap(category
						.getForums())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<Forum> innerItem) {
						Forum forum = innerItem.getModelObject();
						innerItem.add(new Label("name", forum.getName()));
						Link<Forum> moveUpLink = new Link<Forum>("moveup",
								ModelMaker.wrap(forum)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private ForumService forumService;

							@Override
							public void onClick() {
								forumService.moveUp(getModelObject());

								setResponsePage(new ForumManagementPage());
							}

						};

						moveUpLink.setVisible(innerItem.getIndex() != 0);

						moveUpLink.add(new ContextImage("icon",
								"images/icons/arrow_up.png"));

						innerItem.add(moveUpLink);

						Link<Forum> moveDownLink = new Link<Forum>("movedown",
								ModelMaker.wrap(forum)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private ForumService forumService;

							@Override
							public void onClick() {
								forumService.moveDown(getModelObject());

								setResponsePage(new ForumManagementPage());
							}

						};

						moveDownLink.setVisible(innerItem.getIndex() != (total - 1));

						moveDownLink.add(new ContextImage("icon",
								"images/icons/arrow_down.png"));

						innerItem.add(moveDownLink);

						Link<Forum> catPrevLink = new Link<Forum>("catprev",
								ModelMaker.wrap(forum)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private ForumService forumService;

							@Override
							public void onClick() {
								Forum _forum = getModelObject();
								List<ForumCategory> cats = forumCategoryDAO
										.findAll();

								ForumCategory current = _forum.getCategory();

								int index = cats.indexOf(current);

								if (index != -1) {
									int target = index - 1;
									ForumCategory targetCat = cats.get(target);

									forumService.moveToCategory(getUser(),
											_forum, targetCat);

									setResponsePage(new ForumManagementPage());
								}

							}

						};

						catPrevLink.setVisible(catindex != 0
								&& !(forum instanceof GroupForum));

						catPrevLink.add(new ContextImage("icon",
								"images/icons/book_previous.png"));

						innerItem.add(catPrevLink);

						Link<Forum> catNextLink = new Link<Forum>("catnext",
								ModelMaker.wrap(forum)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private ForumService forumService;

							@Override
							public void onClick() {
								Forum _forum = getModelObject();
								List<ForumCategory> cats = forumCategoryDAO
										.findAll();

								ForumCategory current = _forum.getCategory();

								int index = cats.indexOf(current);

								if (index != -1) {
									int target = index + 1;
									ForumCategory targetCat = cats.get(target);

									forumService.moveToCategory(getUser(),
											_forum, targetCat);

									setResponsePage(new ForumManagementPage());
								}

							}

						};

						catNextLink.setVisible(catindex != (forumCategoryDAO
								.countAll() - 1)
								&& !(forum instanceof GroupForum));

						catNextLink.add(new ContextImage("icon",
								"images/icons/book_next.png"));

						innerItem.add(catNextLink);

						Link<Forum> editLink = new Link<Forum>("edit",
								ModelMaker.wrap(forum)) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								setResponsePage(new EditForumPage(
										getModelObject()));
							}

						};

						editLink.add(new ContextImage("icon",
								"images/icons/book_edit.png"));

						editLink.setVisible(!(forum instanceof GroupForum));

						innerItem.add(editLink);

						Link<Forum> moderatorLink = new Link<Forum>(
								"moderators", ModelMaker.wrap(forum)) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								setResponsePage(new EditForumModeratorPage(
										getModelObject()));
							}

						};

						moderatorLink
								.setVisible(!(forum instanceof GroupForum));

						moderatorLink.add(new ContextImage("icon",
								"images/icons/group_edit.png"));

						innerItem.add(moderatorLink);

						Link<Forum> _deleteLink = new Link<Forum>("delete",
								ModelMaker.wrap(forum)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private ForumService forumService;

							@Override
							public void onClick() {
								forumService.deleteForum(getUser(),
										getModelObject());
								setResponsePage(new ForumManagementPage());
							}

						};

						_deleteLink.setVisible(forum.getThreads().isEmpty()
								&& !(forum instanceof NewsForum)
								&& !(forum instanceof GroupForum));

						_deleteLink.add(new ContextImage("icon",
								"images/icons/delete.png"));

						innerItem.add(_deleteLink);

					}

				});

			}

		};
	}

	/**
	 	 */
	private Form<ForumCategory> createAddCategoryForm() {
		Form<ForumCategory> addForm = new Form<ForumCategory>("addform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumService forumService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> nameField = (TextField<String>) get("catname");
				CheckBox checkbox = (CheckBox) get("allowpublic");

				String name = nameField.getModelObject();
				boolean allowPublic = checkbox.getModelObject();

				forumService.createCategory(getUser(), name, allowPublic);

				setResponsePage(new ForumManagementPage());

			}

		};

		addForm.add(new TextField<String>("catname", new Model<String>("")));
		addForm.add(new CheckBox("allowpublic", new Model<Boolean>(false)));
		return addForm;
	}
}
