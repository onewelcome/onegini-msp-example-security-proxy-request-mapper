package com.onegini.examples.service;

import static com.onegini.examples.RequestMapperConstants.AUTHORIZATION_HEADER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Base64;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.onegini.examples.configuration.BasicAuthenticationProperties;
import com.onegini.examples.model.BasicAuthenticationCredentials;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.StatusCodes;

@Service
public class BasicAuthenticationValidationService {

  protected static final String BASIC_AUTH_HEADER_VALUE_PREFIX = "Basic ";
  protected static final String BASIC_AUTH_HEADER_CREDENTIAL_SEPARATOR = ":";

  private static final Logger LOG = LoggerFactory.getLogger(BasicAuthenticationValidationService.class);

  @Resource
  private BasicAuthenticationProperties basicAuthenticationProperties;

  public void validateAuthenticationHeader(final HttpServerExchange httpServerExchange) {

    final Optional<BasicAuthenticationCredentials> basicAuthenticationCredentials = getBasicAuthenticationCredentials(httpServerExchange);

    if(isValidCredentials(basicAuthenticationCredentials)){
      return;
    }
    respondAuthenticationFailed(httpServerExchange);
  }

  private Optional<BasicAuthenticationCredentials> getBasicAuthenticationCredentials(final HttpServerExchange httpServerExchange) {
    final HeaderMap requestHeaders = httpServerExchange.getRequestHeaders();
    final HeaderValues authenticationHeaderValues = requestHeaders.get(AUTHORIZATION_HEADER);
    if(authenticationHeaderValues == null || authenticationHeaderValues.isEmpty()) {
      return Optional.empty();
    }
    return getBasicAuthenticationCredentials(authenticationHeaderValues.getFirst());
  }

  private Optional<BasicAuthenticationCredentials> getBasicAuthenticationCredentials(final String authenticationHeader) {
    final String encodedUsernamePassword = authenticationHeader.replace(BASIC_AUTH_HEADER_VALUE_PREFIX, EMPTY).trim();
    final String usernamePassword;
    try {
      usernamePassword = new String(Base64.getDecoder().decode(encodedUsernamePassword.getBytes(UTF_8)));
    }
    catch (final IllegalArgumentException e) {
      LOG.warn("Failed to decode auth header, ", e);
      return Optional.empty();
    }

    final StringTokenizer tokenizer = new StringTokenizer(usernamePassword, BASIC_AUTH_HEADER_CREDENTIAL_SEPARATOR);
    if (tokenizer.countTokens() == 2) {
      final String headerUsername = tokenizer.nextToken();
      final String headerPassword = tokenizer.nextToken();

      return Optional.of(new BasicAuthenticationCredentials(headerUsername, headerPassword));
    }
    return Optional.empty();
  }

  private boolean isValidCredentials(final Optional<BasicAuthenticationCredentials> basicAuthenticationCredentials) {
    return basicAuthenticationCredentials.filter(this::isUsernameAndPasswordValid).isPresent();
  }

  private boolean isUsernameAndPasswordValid(final BasicAuthenticationCredentials credentials) {
    return basicAuthenticationProperties.getUsername().equals(credentials.getUsername())
        && basicAuthenticationProperties.getPassword().equals(credentials.getPassword());
  }

  private void respondAuthenticationFailed(final HttpServerExchange httpServerExchange) {
    httpServerExchange.setResponseCode(StatusCodes.UNAUTHORIZED);
    httpServerExchange.getResponseSender().close();
  }

}
