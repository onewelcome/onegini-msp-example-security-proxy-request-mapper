package com.onegini.examples.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenValidationResult {

  private String referenceId;

  private String scope;

  public TokenValidationResult() {
  }

  public String getReferenceId() {
    return referenceId;
  }

  public String getScope() {
    return scope;
  }
}
