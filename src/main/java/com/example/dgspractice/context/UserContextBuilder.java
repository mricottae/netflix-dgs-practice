package com.example.dgspractice.context;

import com.netflix.graphql.dgs.context.DgsCustomContextBuilderWithRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Builds the request context from the X-User header.
 *
 * <p>This is authentication-shaped, not authentication: any client can send any header.
 * Real auth would validate a token (Spring Security) and put the resulting principal here.
 */
@Component
public class UserContextBuilder implements DgsCustomContextBuilderWithRequest<UserContext> {

    public static final String USER_HEADER = "X-User";

    @Override
    public UserContext build(Map<String, ?> extensions, HttpHeaders headers, WebRequest webRequest) {
        return new UserContext(headers == null ? null : headers.getFirst(USER_HEADER));
    }
}
