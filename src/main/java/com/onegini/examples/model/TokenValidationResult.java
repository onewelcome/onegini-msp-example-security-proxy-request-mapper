package com.onegini.examples.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenValidationResult {

  private String sub;

  private String scope;

  private List<String> amr;

  public TokenValidationResult() {
  }

  public String getSub() {
    return sub;
  }

  public String getScope() {
    return scope;
  }

  public List<String> getAmr() {
    return amr;
  }
}
