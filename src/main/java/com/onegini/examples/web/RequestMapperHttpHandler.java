package com.onegini.examples.web;

import static io.undertow.util.Headers.CONTENT_TYPE;

import java.io.IOException;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onegini.examples.model.RequestMapperRequest;
import com.onegini.examples.service.BasicAuthenticationValidationService;
import com.onegini.examples.service.TokenValidationResultMappingService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class RequestMapperHttpHandler implements HttpHandler {

  private final ObjectMapper objectMapper;

  @Resource
  private BasicAuthenticationValidationService basicAuthenticationValidationService;
  @Resource
  private
  TokenValidationResultMappingService requestModifier;

  public RequestMapperHttpHandler(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {
    validateBasicAuthHeader(httpServerExchange);
    final RequestMapperRequest request = readRequest(httpServerExchange);
    final RequestMapperRequest modifiedRequest = modifyRequest(request);

    sendModifiedRequestBack(httpServerExchange, modifiedRequest);
  }

  private void validateBasicAuthHeader(final HttpServerExchange httpServerExchange) throws JsonProcessingException {
    basicAuthenticationValidationService.validateAuthenticationHeader(httpServerExchange);
  }

  private RequestMapperRequest readRequest(final HttpServerExchange httpServerExchange) throws IOException {
    return objectMapper.readValue(httpServerExchange.getInputStream(), RequestMapperRequest.class);
  }

  private RequestMapperRequest modifyRequest(final RequestMapperRequest request) {
    return requestModifier.modifyRequest(request);
  }

  private void sendModifiedRequestBack(final HttpServerExchange httpServerExchange, final RequestMapperRequest modifiedRequest) throws JsonProcessingException {
    httpServerExchange.getResponseSender().send(objectMapper.writeValueAsString(modifiedRequest));
    httpServerExchange.getResponseHeaders().add(CONTENT_TYPE, "application/json");
    httpServerExchange.getResponseSender().close();
  }
}