package org.once_a_day.events.idp;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyEventListenerProvider implements EventListenerProvider {
    private final KeycloakSession keycloakSession;
    private final RealmModel realm;
    private final RestTemplateService restTemplateService;
    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

    public MyEventListenerProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
        this.realm = keycloakSession.getContext().getRealm();
        this.restTemplateService = new RestTemplateService();
    }

    @Override
    public void onEvent(Event event) {
        String userId = event.getUserId();
        System.out.println(event.getType());
        switch (event.getType()) {
            case REGISTER -> this.register(userId);
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }

    private void register(String userId) {
        try {
            restTemplateService.register(userId);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }
}
