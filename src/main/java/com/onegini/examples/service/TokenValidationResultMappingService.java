package com.onegini.examples.service;

import static com.onegini.examples.RequestMapperConstants.AUTHORIZATION_HEADER;
import static com.onegini.examples.RequestMapperConstants.SCOPES_HEADER;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.onegini.examples.model.RequestMapperRequest;
import com.onegini.examples.model.TokenValidationResult;

@Service
public class TokenValidationResultMappingService {

  private static final String[] BLACKLISTED_HEADERS = {AUTHORIZATION_HEADER, SCOPES_HEADER};

  public RequestMapperRequest modifyRequest(final RequestMapperRequest request) {
    removeBlacklistedHeaders(request);
    mapUserId(request);
    mapScopes(request);
    return request;
  }

  private void mapUserId(final RequestMapperRequest request) {
    String requestUri = request.getRequestUri();
    final TokenValidationResult tokenValidationResult = request.getTokenValidationResult();
    final String userId = tokenValidationResult.getReferenceId();

    request.setRequestUri(requestUri.concat("/" + userId));
  }

  private void removeBlacklistedHeaders(final RequestMapperRequest request) {
    final Map<String, String> headers = request.getHeaders();
    headers.keySet().removeAll(Arrays.asList(BLACKLISTED_HEADERS));
  }

  private void mapScopes(final RequestMapperRequest request) {
    final Map<String, String> headers = request.getHeaders();
    final TokenValidationResult tokenValidationResult = request.getTokenValidationResult();
    final String scope = tokenValidationResult.getScope();

    headers.put(SCOPES_HEADER, scope);
  }
}
