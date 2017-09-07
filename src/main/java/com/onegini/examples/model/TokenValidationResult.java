package com.onegini.examples.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenValidationResult {

  private String sub;

  private String scope;

  public TokenValidationResult() {
  }

  public String getSub() {
    return sub;
  }

  public String getScope() {
    return scope;
  }
}
