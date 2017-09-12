package com.onegini.examples.service;

import static com.onegini.examples.RequestMapperConstants.TOKEN_TYPE_HEADER;
import static com.onegini.examples.RequestMapperConstants.AUTHORIZATION_HEADER;
import static com.onegini.examples.RequestMapperConstants.SCOPES_HEADER;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.onegini.examples.model.RequestMapperRequest;
import com.onegini.examples.model.TokenValidationResult;

@Service
public class TokenValidationResultMappingService {

  private static final String[] BLACKLISTED_HEADERS = {AUTHORIZATION_HEADER, SCOPES_HEADER, TOKEN_TYPE_HEADER};

  public RequestMapperRequest modifyRequest(final RequestMapperRequest request) {
    removeBlacklistedHeaders(request);
    mapUserId(request);
    mapScopes(request);
    mapAmr(request);
    return request;
  }

  private void mapUserId(final RequestMapperRequest request) {
    final String requestUri = request.getRequestUri();
    final TokenValidationResult tokenValidationResult = request.getTokenValidationResult();
    final String userId = tokenValidationResult.getSub();

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

  private void mapAmr(final RequestMapperRequest request) {
    final Map<String, String> headers = request.getHeaders();
    final TokenValidationResult tokenValidationResult = request.getTokenValidationResult();
    final List<String> amr = tokenValidationResult.getAmr();

    headers.put(TOKEN_TYPE_HEADER, new Gson().toJson(amr));
  }
}
