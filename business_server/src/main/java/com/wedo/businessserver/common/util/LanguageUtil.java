package com.wedo.businessserver.common.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageUtil {
	private static final ResourceBundle MESSAGE = getLocalMessage();

	private static ResourceBundle getLocalMessage() {
		Locale defaultLocale = Locale.getDefault();
		String language = defaultLocale.getLanguage();
		String country = defaultLocale.getCountry();
		Locale locale = new Locale(language, country);
		ResourceBundle bundle = null;

		bundle = ResourceBundle.getBundle("languageproperties/LocalMsg_en_US",
				locale);

		return bundle;
	}

	public static ResourceBundle getMessage() {
		return MESSAGE;
	}
}
