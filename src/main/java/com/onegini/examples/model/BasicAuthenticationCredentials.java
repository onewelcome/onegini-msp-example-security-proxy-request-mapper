package com.onegini.examples.model;

public class BasicAuthenticationCredentials {
  private final String username, password;

  public BasicAuthenticationCredentials(final String username, final String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
