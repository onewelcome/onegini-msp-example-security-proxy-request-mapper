package com.onegini.examples;

import static io.undertow.util.Headers.CONTENT_TYPE;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onegini.examples.model.Request;
import com.onegini.examples.model.TokenValidationResult;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class RequestMapperHttpHandler implements HttpHandler {

  private final ObjectMapper objectMapper;

  private final String SCOPES_HEADER = "Authorized_scopes";
  private final String AUTHORIZATION_HEADER = "Authorization";

  public RequestMapperHttpHandler(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {
    final Request request = readRequest(httpServerExchange);
    final Request modifiedRequest = modifyRequest(request);

    sendModifiedRequestBack(httpServerExchange, modifiedRequest);
  }

  private Request readRequest(final HttpServerExchange httpServerExchange) throws IOException {
    return objectMapper.readValue(httpServerExchange.getInputStream(), Request.class);
  }

  private void sendModifiedRequestBack(final HttpServerExchange httpServerExchange, final Request modifiedRequest) throws JsonProcessingException {
    httpServerExchange.getResponseSender().send(objectMapper.writeValueAsString(modifiedRequest));
    httpServerExchange.getResponseHeaders().add(CONTENT_TYPE, "application/json");
    httpServerExchange.getResponseSender().close();
  }

  private Request modifyRequest(final Request request) {
    final Map<String, String> headers = request.getHeaders();
    String requestUri = request.getRequestUri();
    final TokenValidationResult tokenValidationResult = request.getTokenValidationResult();
    final String scope = tokenValidationResult.getScope();
    final String userId = tokenValidationResult.getReferenceId();

    request.setRequestUri(requestUri.concat("/"+userId));
    removeBlacklistedHeaders(headers);
    headers.put(SCOPES_HEADER, scope);
    return request;
  }

  private void removeBlacklistedHeaders(final Map<String, String> headers) {
    headers.remove(AUTHORIZATION_HEADER);
    headers.remove(SCOPES_HEADER);
  }
}