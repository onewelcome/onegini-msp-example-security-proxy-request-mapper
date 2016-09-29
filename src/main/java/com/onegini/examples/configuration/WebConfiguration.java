package com.onegini.examples.configuration;

import static io.undertow.util.Methods.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.onegini.examples.RequestMapperHttpHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.AllowedMethodsHandler;
import io.undertow.server.handlers.BlockingHandler;

@Configuration
@EnableConfigurationProperties(UndertowProperties.class)
public class WebConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebConfiguration.class);

  @Autowired
  private HttpHandler httpHandler;
  @Autowired
  private UndertowProperties undertowProperties;

  @Bean
  public HttpHandler httpHandler(final ObjectMapper objectMapper) {
    final RequestMapperHttpHandler requestMapperHttpHandler = new RequestMapperHttpHandler(objectMapper);
    final AllowedMethodsHandler postHandler = new AllowedMethodsHandler(requestMapperHttpHandler, POST);
    return new BlockingHandler(Handlers.path().addExactPath("/map-request", postHandler));
  }

  @Bean
  public Undertow undertow() {
    final Undertow undertow = Undertow.builder()
        .addHttpListener(undertowProperties.getPort(), undertowProperties.getHost())
        .setHandler(httpHandler)
        .build();
    undertow.start();
    LOGGER.info("Undertow server started up on {}:{}", undertowProperties.getHost(), undertowProperties.getPort());
    return undertow;
  }

  @Bean
  public ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.registerModule(new AfterburnerModule());
    return objectMapper;
  }
}
