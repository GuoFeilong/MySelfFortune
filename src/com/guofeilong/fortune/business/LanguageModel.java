package com.guofeilong.fortune.business;

import java.io.Serializable;
import java.util.Locale;


public class LanguageModel implements Serializable {

	private String languageName;
	public boolean checkedLanguageFlag;
	private Locale locale;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getLanguageName() {
		return languageName;
	}

	public boolean isCheckedLanguageFlag() {
		return checkedLanguageFlag;
	}

	public LanguageModel() {
		super();
		this.locale = locale;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public void setCheckedLanguageFlag(boolean checkedLanguageFlag) {
		this.checkedLanguageFlag = checkedLanguageFlag;
	}

}
