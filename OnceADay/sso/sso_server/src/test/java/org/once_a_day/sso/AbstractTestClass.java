package org.once_a_day.sso;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public abstract class AbstractTestClass {
    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/once_a_day");
    }

    private static final KeycloakContainer keycloak;
    protected static RestTemplate restTemplate;
    protected static MockRestServiceServer mockServer;

    static {
        keycloak = new KeycloakContainer().withRealmImportFile("once_a_day-realm.json");
        keycloak.start();
        restTemplate=new RestTemplate();
        final String token = token();
        restTemplate = new RestTemplateBuilder()
                .defaultHeader(AUTHORIZATION, token)
                .build();
    }

    protected static String token() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = keycloak.getAuthServerUrl() + "/realms/once_a_day/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("OAD"));
        formData.put("username", Collections.singletonList("kamil"));
        formData.put("password", Collections.singletonList("kamil"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return "Bearer " + jsonParser.parseMap(response.getBody())
                .get("access_token")
                .toString();
    }
}
