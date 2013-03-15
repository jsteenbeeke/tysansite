package com.tysanclan.site.projectewok.pages.member;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment.UnpaidFilter;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionPaymentDAO;
import com.tysanclan.site.projectewok.model.DollarSignModel;

@TysanMemberSecured
public class SubscriptionPaymentPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private SubscriptionPaymentDAO paymentDAO;

	public SubscriptionPaymentPage() {
		super("Subscription payments due");

		if (roleService.getTreasurer() == null
				|| roleService.getTreasurer().getPaypalAddress() == null) {
			OverviewPage p = new OverviewPage();
			p.error("No Treasurer, or Treasurer has not setup Paypal");

			throw new RestartResponseAtInterceptPageException(p);
		}

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		add(accordion);

		accordion.add(new ListView<SubscriptionPayment>("requests", ModelMaker
				.wrap(ImmutableList.copyOf(Iterables.filter(getUser()
						.getPayments(), new UnpaidFilter())))) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SubscriptionPayment> item) {
				SubscriptionPayment payment = item.getModelObject();

				DateTime dt = new DateTime(payment.getDate());
				String dateTimeStr = dt.toString("dd MMMM yyyy", Locale.US);

				item.add(new Label("header", "Subscription Payment - "
						+ dateTimeStr));
				item.add(new Label("amount", new DollarSignModel(
						new Model<BigDecimal>(payment.getSubscription()
								.getAmount()))));

				item.add(new HiddenField<String>(
						"paypalAddress",
						new Model<String>(
								roleService.getTreasurer() != null ? roleService
										.getTreasurer().getPaypalAddress()
										: null)).add(AttributeModifier.replace(
						"name", "business")));
				item.add(new HiddenField<String>("itemname", new Model<String>(
						"The Tysan Clan - Subscription Payment - "
								+ dateTimeStr)));

				item.add(new HiddenField<String>("itemdesc", new Model<String>(
						"The Tysan Clan - Subscription Payment - "
								+ dateTimeStr)).add(AttributeModifier.replace(
						"name", "item_name"))); //
				item.add(new HiddenField<String>("amount2", new Model<String>(
						payment.getSubscription().getAmount().toString()))
						.add(AttributeModifier.replace("name", "amount")));
				item.add(new HiddenField<String>("returnUrl",
						new Model<String>(
								"https://www.tysanclan.com/processSubscriptionPayment/"
										+ payment.getId()
										+ "/"
										+ paymentDAO
												.getConfirmationKey(payment)
										+ "/")).add(AttributeModifier.replace(
						"name", "return")));
			}
		});
	}
}
