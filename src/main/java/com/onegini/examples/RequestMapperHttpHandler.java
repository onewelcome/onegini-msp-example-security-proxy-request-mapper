package com.onegini.examples;

import static io.undertow.util.Headers.CONTENT_TYPE;

import java.io.IOException;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onegini.examples.model.Request;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class RequestMapperHttpHandler implements HttpHandler {

  private final ObjectMapper objectMapper;

  @Resource
  private BasicAuthenticationValidationService basicAuthenticationValidationService;
  @Resource
  private
  RequestModifier requestModifier;

  public RequestMapperHttpHandler(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {
    validateBasicAuthHeader(httpServerExchange);
    final Request request = readRequest(httpServerExchange);
    final Request modifiedRequest = modifyRequest(request);

    sendModifiedRequestBack(httpServerExchange, modifiedRequest);
  }

  private void validateBasicAuthHeader(final HttpServerExchange httpServerExchange) throws JsonProcessingException {
    basicAuthenticationValidationService.validateAuthenticationHeader(httpServerExchange);
  }

  private Request readRequest(final HttpServerExchange httpServerExchange) throws IOException {
    return objectMapper.readValue(httpServerExchange.getInputStream(), Request.class);
  }

  private Request modifyRequest(final Request request) {
    return requestModifier.modifyRequest(request);
  }

  private void sendModifiedRequestBack(final HttpServerExchange httpServerExchange, final Request modifiedRequest) throws JsonProcessingException {
    httpServerExchange.getResponseSender().send(objectMapper.writeValueAsString(modifiedRequest));
    httpServerExchange.getResponseHeaders().add(CONTENT_TYPE, "application/json");
    httpServerExchange.getResponseSender().close();
  }
}