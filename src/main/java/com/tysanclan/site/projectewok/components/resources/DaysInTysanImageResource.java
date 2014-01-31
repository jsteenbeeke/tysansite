package com.tysanclan.site.projectewok.components.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.ContextPathGenerator;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;

public class DaysInTysanImageResource extends AbstractDynamicTysanImage {

	private static final long serialVersionUID = 1L;

	@Override
	protected List<DynamicImageTextPoint> getTexts(User user) {
		List<DynamicImageTextPoint> texts = Lists.newArrayList();
	    texts.add( new DynamicImageTextPoint(user.getUsername(), 50, 150){
	    	@Override
	    	protected void onBeforeDraw(Graphics graphics) {
	    		graphics.setColor(Color.black);
	    		graphics.setFont(new Font("Brush Script MT", Font.BOLD, 20));
	    	}
	    });
	    
	    int numberOfMemberDays = DateUtil.daysBetween(user.getJoinDate(), new Date());
	    texts.add( new DynamicImageTextPoint(numberOfMemberDays + " days!", 30, 100){
	    	@Override
	    	protected void onBeforeDraw(Graphics graphics) {
	    		graphics.setColor(Color.black);
	    		graphics.setFont(new Font("Brush Script MT", Font.BOLD, 20));
	    	}
	    });
   
	    return texts;
	}

	@Override
	protected BufferedImage getImage(User user) {
		return getBufferedImageFromURL(DaysInTysanImageResource.class.getResource("daysspendintysan.png").toString());
	}
}
