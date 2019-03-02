package com.tysanclan.site.projectewok.components.resources;

import com.google.common.collect.Lists;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MathUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

public class HaleyAccidentImage extends AbstractDynamicTysanImage {
	private static final long serialVersionUID = 1L;
	private static final Font FONT = new Font("Franchise Bold", Font.PLAIN, 85);

	private static final int PIXELS_PER_LETTER = 30;

	private static final int IMAGE_WIDTH = 400;

	private static final int Y_INDEX = 190;

	private final String HALEY = "ivymoonxoxo";

	@Override
	protected byte[] getImageData(Attributes attributes) {
		attributes.getParameters().add(getUsernameParameter(), HALEY);

		return super.getImageData(attributes);
	}

	@Override
	protected List<DynamicImageTextPoint> getTexts(User user) {
		List<DynamicImageTextPoint> texts = Lists.newArrayList();

		final int numberOfMemberDays = DateUtil
				.daysBetween(user.getJoinDate(), new Date());

		final int digits = MathUtil.countPrintableDigits(numberOfMemberDays);
		final int width = PIXELS_PER_LETTER * (digits + 5);
		final int x = (IMAGE_WIDTH - width) / 2;

		texts.add(
				new DynamicImageTextPoint(Integer.toString(numberOfMemberDays),
						x, Y_INDEX) {
					@Override
					protected void onBeforeDraw(Graphics graphics) {
						graphics.setColor(Color.red);
						graphics.setFont(FONT);
					}
				});
		texts.add(new DynamicImageTextPoint("days",
				x + (PIXELS_PER_LETTER) * (digits + 1), Y_INDEX) {
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
		return getBufferedImageFromURL(
				DaysInTysanImageResource.class.getResource("HaleyRejoin.png")
						.toString());
	}

}
