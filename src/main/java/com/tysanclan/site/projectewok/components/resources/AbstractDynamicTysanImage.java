package com.tysanclan.site.projectewok.components.resources;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import io.vavr.collection.Seq;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.string.StringValue;

import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;

/**
 * Abstract class to easily add text to an image, so we can create many custom signatures and such
 * For this to work, the attribute 
 * @author ties
 *
 */
public abstract class AbstractDynamicTysanImage extends DynamicImageResource {

	private static final long serialVersionUID = 1L;

	@Override
	protected byte[] getImageData(Attributes attributes) {
		PageParameters parameters = attributes.getParameters();
		StringValue name = parameters.get(getUsernameParameter());
		if (name.isNull()) {
			return getErrorImage();
		}

		UserFilter filter = new UserFilter();
		filter.username(name.toOptionalString());
		Seq<User> users = TysanApplication.getApplicationContext()
										  .getBean(UserDAO.class).findByFilter(filter);
		if (users == null || users.isEmpty()) {
			return getErrorImage();
		}
		User user = users.get(0);

		BufferedImage image = getImage(user);
		if (image == null) {
			return getErrorImage();
		}
		Graphics graphics = image.getGraphics();

		for (DynamicImageTextPoint text : getTexts(user)) {
			text.onBeforeDraw(graphics);
			graphics.drawString(text.getText(), text.getX(), text.getY());
		}
		graphics.dispose();
		return toImageData(image);
	}

	private byte[] getErrorImage() {
		return null;
	}

	protected String getUsernameParameter() {
		return "username";
	}

	protected abstract List<DynamicImageTextPoint> getTexts(User user);

	protected abstract BufferedImage getImage(User user);

	protected BufferedImage getBufferedImageFromURL(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return null;
	}

}
