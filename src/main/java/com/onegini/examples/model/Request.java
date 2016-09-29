package com.onegini.examples.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

  private Map<String, String> headers;

  private String requestUri;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private TokenValidationResult tokenValidationResult;

  public Request() {
  }

  public Request(final Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(final String requestUri) {
    this.requestUri = requestUri;
  }

  public TokenValidationResult getTokenValidationResult() {
    return tokenValidationResult;
  }
}
