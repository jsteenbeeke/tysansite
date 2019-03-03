package com.tysanclan.site.projectewok.util;

import com.google.common.base.Function;

import java.io.Serializable;

public interface SerializableFunction<F, T>
		extends Serializable, Function<F, T> {

}
