package com.tysanclan.site.projectewok.components.resources;

import com.google.common.collect.Lists;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

public class DaysInTysanImageResource extends AbstractDynamicTysanImage {

	private static final Font FONT = new Font("Brush Script MT", Font.BOLD, 20);

	private static final long serialVersionUID = 1L;

	@Override
	protected List<DynamicImageTextPoint> getTexts(User user) {
		List<DynamicImageTextPoint> texts = Lists.newArrayList();
		texts.add(new DynamicImageTextPoint(user.getUsername(), 50, 150) {
			@Override
			protected void onBeforeDraw(Graphics graphics) {
				graphics.setColor(Color.black);
				graphics.setFont(FONT);
			}
		});

		int numberOfMemberDays = DateUtil
				.daysBetween(user.getJoinDate(), new Date());
		texts.add(new DynamicImageTextPoint(numberOfMemberDays + " days!", 30,
				100) {
			@Override
			protected void onBeforeDraw(Graphics graphics) {
				graphics.setColor(Color.black);
				graphics.setFont(FONT);
			}
		});

		return texts;
	}

	@Override
	protected BufferedImage getImage(User user) {
		return getBufferedImageFromURL(DaysInTysanImageResource.class
				.getResource("daysspendintysan.png").toString());
	}
}
