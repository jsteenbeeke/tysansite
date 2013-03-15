package com.tysanclan.site.projectewok.components;

import org.apache.wicket.request.resource.ByteArrayResource;

import com.tysanclan.site.projectewok.util.ImageUtil;

public class StoredImageResource extends ByteArrayResource {
	private static final long serialVersionUID = 1L;

	public StoredImageResource(byte[] image) {
		super(ImageUtil.getMimeType(image), image);
	}
}
