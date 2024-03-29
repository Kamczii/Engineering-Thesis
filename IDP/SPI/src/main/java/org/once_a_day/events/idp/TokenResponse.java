package org.once_a_day.events.idp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    String access_token;
    Long expires_in;
    Long refresh_expires_in;
    String token_type;
    @JsonProperty("not-before-policy")
    Long notbeforepolicy;
    String scope;

    public TokenResponse() {
    }

    public TokenResponse(String access_token, Long expires_in, Long refresh_expires_in, String token_type, Long notbeforepolicy, String scope) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_expires_in = refresh_expires_in;
        this.token_type = token_type;
        this.notbeforepolicy = notbeforepolicy;
        this.scope = scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public Long getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public void setRefresh_expires_in(Long refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getNotbeforepolicy() {
        return notbeforepolicy;
    }

    public void setNotbeforepolicy(Long notbeforepolicy) {
        this.notbeforepolicy = notbeforepolicy;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
