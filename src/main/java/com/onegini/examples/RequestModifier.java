package com.onegini.examples;

import static com.onegini.examples.RequestMapperConstants.*;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.onegini.examples.model.Request;
import com.onegini.examples.model.TokenValidationResult;

@Service
public class RequestModifier {

  public Request modifyRequest(final Request request) {
    final Map<String, String> headers = request.getHeaders();
    String requestUri = request.getRequestUri();
    final TokenValidationResult tokenValidationResult = request.getTokenValidationResult();
    final String scope = tokenValidationResult.getScope();
    final String userId = tokenValidationResult.getReferenceId();

    appendUserIdToRequestUri(request, requestUri, userId);
    removeBlacklistedHeaders(headers);
    addScopesHeader(headers, scope);
    return request;
  }

  private void appendUserIdToRequestUri(final Request request, final String requestUri, final String userId) {
    request.setRequestUri(requestUri.concat("/"+userId));
  }

  private void removeBlacklistedHeaders(final Map<String, String> headers) {
    headers.remove(AUTHORIZATION_HEADER);
    headers.remove(SCOPES_HEADER);
  }

  private void addScopesHeader(final Map<String, String> headers, final String scope) {
    headers.put(SCOPES_HEADER, scope);
  }
}
