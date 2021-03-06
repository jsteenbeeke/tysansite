package com.tysanclan.site.projectewok.components.resources;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.util.MemberUtil;
import io.vavr.collection.Seq;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Time;

import java.util.ArrayList;

public class UUIDMinecraftWhitelistResource extends AbstractResource {

	private static final long serialVersionUID = 1L;

	public String getCurrentWhitelist() {

		Seq<Game> games = TysanApplication.get().getApplicationContext()
				.getBean(GameDAO.class).findAll();
		ArrayList<String> whitelist = new ArrayList<String>();

		StringBuilder whiteListBuilder = new StringBuilder();

		for (Game g : games) {
			for (UserGameRealm ugr : g.getPlayers()) {
				for (GameAccount acc : ugr.getAccounts()) {
					if (acc.getType() == AccountType.MINECRAFT && MemberUtil
							.isMember(ugr.getUser())) {
						whitelist.add(acc.getName());
					}
				}
			}
		}

		String[] usernames = whitelist.toArray(new String[whitelist.size()]);

		HttpProfileRepository repo = new HttpProfileRepository("minecraft");

		Profile[] profiles = repo.findProfilesByNames(usernames);

		final String newline = System.getProperty("line.separator");

		whiteListBuilder.append("[").append(newline);
		int i = 0;
		for (Profile profile : profiles) {
			if (i++ > 0) {
				whiteListBuilder.append(",").append(newline);
			}
			whiteListBuilder.append("\t{").append(newline);
			whiteListBuilder.append("\t\t\"uuid\": \"")
					.append(formatUUID(profile.getId())).append("\",")
					.append(newline);
			whiteListBuilder.append("\t\t\"name\": \"")
					.append(profile.getName()).append("\"").append(newline);
			whiteListBuilder.append("\t}");
		}
		whiteListBuilder.append(newline).append("]").append(newline);

		return whiteListBuilder.toString();

	}

	private String formatUUID(String id) {
		if (id.length() == 32) {
			return String.format("%s-%s-%s-%s-%s",//
					id.substring(0, 8), //
					id.substring(8, 12), //
					id.substring(12, 16), //
					id.substring(16, 20), //
					id.substring(20, 32) //
			);
		}

		return id;
	}

	@Override
	protected ResourceResponse newResourceResponse(
			final Attributes attributes) {
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
