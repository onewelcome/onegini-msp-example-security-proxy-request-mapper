package com.onegini.examples.configuration;

import static io.undertow.util.Methods.POST;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.onegini.examples.service.BasicAuthIdentityManager;
import com.onegini.examples.web.RequestMapperHttpHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.AllowedMethodsHandler;
import io.undertow.server.handlers.BlockingHandler;

@Configuration
@EnableConfigurationProperties(UndertowProperties.class)
public class RequestMapperConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestMapperConfiguration.class);

  @Autowired
  private HttpHandler httpHandler;
  @Autowired
  private UndertowProperties undertowProperties;

  @Bean
  public RequestMapperHttpHandler requestMapperHttpHandler(final ObjectMapper objectMapper) {
    return new RequestMapperHttpHandler(objectMapper);
  }

  @Bean
  public HttpHandler httpHandler(final RequestMapperHttpHandler requestMapperHttpHandler) {
    final AllowedMethodsHandler postHandler = new AllowedMethodsHandler(requestMapperHttpHandler, POST);
    return new BlockingHandler(Handlers.path().addExactPath("/map-request", postHandler));
  }

  @Bean
  @ConfigurationProperties(prefix = "basic.authentication")
  public BasicAuthenticationProperties basicAuthenticationProperties() {
    return new BasicAuthenticationProperties();
  }

  @Bean
  public BasicAuthIdentityManager basicAuthIdentityManager(BasicAuthenticationProperties basicAuthenticationProperties) {
    return new BasicAuthIdentityManager(basicAuthenticationProperties);
  }

  @Bean
  public Undertow undertow(final BasicAuthIdentityManager basicAuthIdentityManager) {
    final Undertow undertow = Undertow.builder()
        .addHttpListener(undertowProperties.getPort(), undertowProperties.getHost())
        .setHandler(addSecurity(httpHandler, basicAuthIdentityManager))
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

  private static HttpHandler addSecurity(final HttpHandler toWrap, final IdentityManager identityManager) {
    HttpHandler handler = toWrap;
    handler = new AuthenticationCallHandler(handler);
    handler = new AuthenticationConstraintHandler(handler);
    final List<AuthenticationMechanism> mechanisms = Collections.<AuthenticationMechanism>singletonList(new BasicAuthenticationMechanism("My Realm"));
    handler = new AuthenticationMechanismsHandler(handler, mechanisms);
    handler = new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE, identityManager, handler);
    return handler;
  }
}
