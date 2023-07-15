package com.saathratri.orchestrator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Chatbotorchestrator.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String allAllowedOrigins;
    private String textFrom;
    private String textTo;
	private String messagesVersion;

    public String getAllAllowedOrigins() {
        return allAllowedOrigins;
    }

    public void setAllAllowedOrigins(String allAllowedOrigins) {
        this.allAllowedOrigins = allAllowedOrigins;
    }

	/**
	 * @return the textFrom
	 */
	public String getTextFrom() {
		return textFrom.trim();
	}

	/**
	 * @param textFrom the textFrom to set
	 */
	public void setTextFrom(String textFrom) {
		this.textFrom = textFrom.trim();
	}

	/**
	 * @return the textTo
	 */
	public String getTextTo() {
		return textTo.trim();
	}
	/**
	 * @param textTo the textTo to set
	 */
	public void setTextTo(String textTo) {
		this.textTo = textTo.trim();
	}

	/**
	 * @return the messagesVersion
	 */
	public String getMessagesVersion() {
		return messagesVersion.trim();
	}
	/**
	 * @param messagesVersion the messagesVersion to set
	 */
	public void setMessagesVersion(String messagesVersion) {
		this.messagesVersion = messagesVersion.trim();
	}
}
