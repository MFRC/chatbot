package com.saathratri.orchestrator.config;

/**
 * Application constants.
 */
public final class Constants {
    public static final Integer MAX_RATING = 5;

    public static final String GOOGLE_REVIEW_CATEGORY = "Google Review";

    public static final String LANDING_PAGE_SOURCE = "Landing Page";



    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";
	
	private Constants() {}
}
