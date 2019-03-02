package com.tysanclan.site.projectewok.components;

import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil.BBCodeVerification;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.RawValidationError;

public class BBCodeValidator implements IValidator<String> {
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(IValidatable<String> validatable) {
		BBCodeVerification verify = BBCodeUtil.verify(validatable.getValue());

		if (!verify.isOk()) {
			validatable.error(new RawValidationError(verify.getErrorMessage()));
		}
	}
}
