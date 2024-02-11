package org.once_a_day.events.idp;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class RestTemplateService {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String REGISTER_ENDPOINT = "http://keycloak-sidecar:8084/oad-sso/users/register";
    private final String TOKEN_ENDPOINT = "http://x.x.x.x:8080/realms/once_a_day/protocol/openid-connect/token";
    private final String GRANT_TYPE = "client_credentials";
    private final String CLIENT_ID = "keycloak";
    private final String CLIENT_SECRET = "GST9cP52uKv8wEMru06XM2I2Bc2GnOkn";
    ObjectMapper objectMapper;
    OkHttpClient client;

    public RestTemplateService() {
        client = new OkHttpClient().newBuilder()
                .build();
        objectMapper = new ObjectMapper();
    }

    public void register(String userId) throws IOException, URISyntaxException, InterruptedException {
        RequestBody body = RequestBody.create("\"" + userId + "\"", JSON);

        Request request = new Request.Builder()
                .url(REGISTER_ENDPOINT)
                .post(body)
                .header("Authorization", "Bearer " + getToken())
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            System.out.println("Successful registration for " + userId);
        } else {
            System.out.println("Failed for user with id " + userId);
            System.out.println(response.body().string());
        }
        response.close();


    }

    private String getToken() throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GRANT_TYPE)
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_ENDPOINT)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        TokenResponse entity = objectMapper.readValue(response.body().string(), TokenResponse.class);
        return entity.getAccess_token();
    }
}
