package com.saathratri.orchestrator.config;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StartupApplicationListener implements 
  ApplicationListener<ContextRefreshedEvent> {

  private static final Logger log = LoggerFactory.getLogger(StartupApplicationListener.class);

  @Override public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Starting up Application User Properties Service CRON tasks...");

    UUID taskId = UUID.randomUUID();
  }
}