package org.dice.alk.controller;

import org.dice.alk.nlp.ITextProcessor;
import org.dice.alk.nlp.InputProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * 
 * Configuration file that defines bean creation of classes that either have multiple
 * implementations or depend on application.properties input.
 */
@Configuration
@PropertySource(value = "classpath:application.properties")
public class Config {

  /**
   * WAT API Authorization Token
   */
  @Value("${wat.api.token:}")
  private String token;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /**
   * Bean factory for ITextProcessor
   * 
   * @return the {@link ITextProcessor} object
   */
  @Bean
  public ITextProcessor getInputProcessor() {
    return new InputProcessor(token);
  }

}
