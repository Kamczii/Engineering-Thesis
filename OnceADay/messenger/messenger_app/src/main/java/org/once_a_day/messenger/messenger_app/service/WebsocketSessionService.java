package org.once_a_day.messenger.messenger_app.service;

public interface WebsocketSessionService {
    Long getExp(String sessionId);

    boolean sessionExist(String sessionId);

    void remove(String sessionId);

    void put(String sessionId, String token);
}
