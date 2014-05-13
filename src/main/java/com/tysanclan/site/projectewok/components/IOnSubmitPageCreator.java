package com.tysanclan.site.projectewok.components;

import java.io.Serializable;

import org.apache.wicket.Page;

public interface IOnSubmitPageCreator extends Serializable {
	Page createPage();
}