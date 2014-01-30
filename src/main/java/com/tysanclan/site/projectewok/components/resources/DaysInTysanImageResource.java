package com.tysanclan.site.projectewok.components.resources;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;

public class DaysInTysanImageResource extends AbstractDynamicTysanImage {

	private static final long serialVersionUID = 1L;

	@Override
	protected List<DynamicImageTextPoint> getTexts(User user) {
		List<DynamicImageTextPoint> texts = Lists.newArrayList();
	    texts.add( new DynamicImageTextPoint(user.getUsername(), 100, 50));
	    
	    int numberOfMemberDays = DateUtil.daysBetween(user.getJoinDate(), new Date());
	    texts.add( new DynamicImageTextPoint(numberOfMemberDays + " days!", 100, 100));
	    return texts;
	}

	@Override
	protected BufferedImage getImage(User user) {
		return getBufferedImageFromURL("http://2.bp.blogspot.com/_Vy2Puo54Ko0/TMqTr6iWBcI/AAAAAAAAIUE/iVFYU995KEk/s400/ist2_438171-ancient-scroll.jpg");
	}

	@Override
	protected Font getFont(Graphics graphics) {
		return graphics.getFont().deriveFont(30f);
	}

}
