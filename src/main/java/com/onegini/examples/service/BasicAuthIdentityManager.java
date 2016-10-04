package com.onegini.examples.service;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import com.onegini.examples.configuration.BasicAuthenticationProperties;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;

public class BasicAuthIdentityManager implements IdentityManager {

  private BasicAuthenticationProperties basicAuthenticationProperties;

  public BasicAuthIdentityManager(final BasicAuthenticationProperties basicAuthenticationProperties) {
    this.basicAuthenticationProperties = basicAuthenticationProperties;
  }

  @Override
  public Account verify(Account account) {
    return account;
  }

  @Override
  public Account verify(String id, Credential credential) {
    Account account = getAccount(id);
    if (account != null && verifyCredential(credential)) {
      return account;
    }

    return null;
  }

  @Override
  public Account verify(Credential credential) {
    return null;
  }

  private boolean verifyCredential(Credential credential) {
    if (credential instanceof PasswordCredential) {
      char[] password = ((PasswordCredential) credential).getPassword();
      char[] expectedPassword = basicAuthenticationProperties.getPassword().toCharArray();

      return Arrays.equals(password, expectedPassword);
    }
    return false;
  }

  private Account getAccount(final String id) {
    if (id.equals(basicAuthenticationProperties.getUsername())) {
      return new Account() {

        private final Principal principal = () -> id;

        @Override
        public Principal getPrincipal() {
          return principal;
        }

        @Override
        public Set<String> getRoles() {
          return Collections.emptySet();
        }

      };
    }
    return null;
  }

}
