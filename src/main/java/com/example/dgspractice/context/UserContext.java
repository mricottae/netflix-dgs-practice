package com.example.dgspractice.context;

/**
 * Per-request caller identity. Built once per request by {@link UserContextBuilder}
 * and readable from any datafetcher (or DataLoader) via DgsContext.getCustomContext.
 *
 * @param username the caller, or null when the request carried no X-User header
 */
public record UserContext(String username) {

    public boolean isAuthenticated() {
        return username != null && !username.isBlank();
    }
}
