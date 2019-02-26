package com.tysanclan.site.projectewok.components.resources;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import io.vavr.collection.Seq;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Time;

import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.util.MemberUtil;

public class MinecraftWhitelistResource extends AbstractResource {

	private static final long serialVersionUID = 1L;

	public String getCurrentWhitelist() {

		Seq<Game> games = TysanApplication.get().getApplicationContext()
										  .getBean(GameDAO.class).findAll();
		List<String> whitelist = new LinkedList<String>();

		StringBuilder whiteListBuilder = new StringBuilder();

		for (Game g : games) {
			for (UserGameRealm ugr : g.getPlayers()) {
				for (GameAccount acc : ugr.getAccounts()) {
					if (acc.getType() == AccountType.MINECRAFT
							&& MemberUtil.isMember(ugr.getUser())) {
						whitelist.add(acc.getName());
					}
				}
			}
		}

		Collections.sort(whitelist, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});

		for (String account : whitelist) {
			whiteListBuilder.append(account);
			whiteListBuilder.append(System.getProperty("line.separator"));
		}

		return whiteListBuilder.toString();

	}

	@Override
	protected ResourceResponse newResourceResponse(final Attributes attributes) {
		final ResourceResponse response = new ResourceResponse();

		response.setLastModified(Time.now());

		if (response.dataNeedsToBeWritten(attributes)) {
			response.setContentType("text/plain");

			response.setContentDisposition(ContentDisposition.INLINE);

			response.setWriteCallback(new WriteCallback() {
				@Override
				public void writeData(final Attributes attributes) {
					attributes.getResponse().write(getCurrentWhitelist());
				}
			});
		}

		return response;
	}
}
