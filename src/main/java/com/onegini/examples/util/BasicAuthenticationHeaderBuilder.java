package com.onegini.examples.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Base64;

public class BasicAuthenticationHeaderBuilder {

  private String username;
  private String password;

  public BasicAuthenticationHeaderBuilder withUsername(final String username) {
    this.username = username;
    return this;
  }

  public BasicAuthenticationHeaderBuilder withPassword(final String password) {
    this.password = password;
    return this;
  }

  public String build() {
    final String basicAuthHeader = new String(Base64.getEncoder().encode(username.concat(":").concat(password).getBytes(UTF_8)));
    return String.format("Basic %s", basicAuthHeader);
  }

}
