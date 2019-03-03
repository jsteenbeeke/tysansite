package com.tysanclan.site.projectewok.components;

import org.apache.wicket.Page;

import java.io.Serializable;

public interface IOnSubmitPageCreator extends Serializable {
	Page createPage();
}
