package com.tysanclan.site.projectewok.pages;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanJQueryUIInitialisationResourceReference;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.components.resources.silverblue.SilverblueBootstrapJavaScriptReference;

public enum PageStyle {
	GOLD {
		@Override
		public boolean isCurrent(String style) {
			return style == null || "".equals(style);
		}

		@Override
		public void renderHead(IHeaderResponse response, TysanPage page) {
			response.render(CssHeaderItem.forUrl("css/style.css"));

			response.render(JavaScriptHeaderItem
					.forReference(TysanJQueryUIInitialisationResourceReference
							.get()));

			Integer autoTabIndex = page.getAutoTabIndex();

			if (autoTabIndex != null) {
				response.render(OnDomReadyHeaderItem.forScript(String.format(
						"$('.jqui-tabs-auto').tabs({ active: %d });",
						autoTabIndex)));
			} else {
				response.render(OnDomReadyHeaderItem
						.forScript("$('.jqui-tabs-auto').tabs();"));
			}

			StringBuilder collapsibles = new StringBuilder();
			collapsibles
					.append("$('.jqui-accordion-collapsible').accordion({\n");
			collapsibles.append("\tautoHeight: true,\n");
			collapsibles.append("\theader: 'h2',\n");
			collapsibles.append("\theightStyle: 'content',\n");
			collapsibles.append("\tcollapsible: true,\n");
			if (page.isAutoCollapse()) {
				collapsibles.append("\tactive: false\n");
			} else {
				collapsibles.append("\tactive: 0\n");
			}
			collapsibles.append("});");

			response.render(OnDomReadyHeaderItem.forScript(collapsibles));

			String openFirst = "$('.jqui-accordion-collapsible:first').accordion( 'option', 'active', 0 )";
			response.render(OnDomReadyHeaderItem.forScript(openFirst));

		}
	},
	SILVERBLUE {
		@Override
		public boolean isCurrent(String style) {
			return "silverblue".equals(style);
		}

		@Override
		public void renderHead(IHeaderResponse response, TysanPage page) {
			response.render(JavaScriptHeaderItem
					.forReference(SilverblueBootstrapJavaScriptReference.get()));
		}
	};

	public abstract boolean isCurrent(String style);

	public static PageStyle getCurrentStyle(String currentStyle) {
		for (PageStyle style : values()) {
			if (style.isCurrent(currentStyle)) {
				return style;
			}
		}

		return getDefault();
	}

	private static PageStyle getDefault() {
		return GOLD;
	}

	public abstract void renderHead(IHeaderResponse response, TysanPage page);

	public String getRankImage(Rank rank) {
		String imageLocation;
		StringBuilder rankStr = new StringBuilder();
		String lowerCaseRank = rank.toString().toLowerCase();

		for (int i = 0; i < lowerCaseRank.length(); i++) {
			if (!Character.isWhitespace(lowerCaseRank.charAt(i))) {
				rankStr.append(lowerCaseRank.charAt(i));
			} else {
				rankStr.append("_");
			}
		}

		imageLocation = "images/ranks/" + rankStr.toString() + ".gif";

		if (rank == Rank.FORUM || rank == Rank.BANNED) {
			imageLocation = "images/icons/script.png";
		}
		if (rank == Rank.HERO) {
			imageLocation = "images/icons/rosette.png";
		}

		return imageLocation;
	}
}
