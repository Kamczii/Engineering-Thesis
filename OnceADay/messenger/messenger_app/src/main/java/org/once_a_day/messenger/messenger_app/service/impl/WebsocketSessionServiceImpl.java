package org.once_a_day.messenger.messenger_app.service.impl;

import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.once_a_day.messenger.messenger_app.service.WebsocketSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebsocketSessionServiceImpl implements WebsocketSessionService {

    Logger logger = LoggerFactory.getLogger(WebsocketSessionService.class);

    private final Map<String, Long> map = new HashMap<>();

    @Override
    public Long getExp(final String sessionId) {
        return map.get(sessionId);
    }

    @Override
    public boolean sessionExist(final String sessionId) {
        final var exp = getExp(sessionId);
        return exp != null && exp > now();
    }

    @Override
    public void remove(final String sessionId) {
        map.remove(sessionId);
    }

    @Override
    public void put(final String sessionId, final String token) {
        final var exp = expiresIn(token);
        map.put(sessionId, exp);
    }

    private Long expiresIn(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        final String[] chunks = token.split("\\.");
        final String payload = new String(decoder.decode(chunks[1]));
        final var json = JsonParser.parseString(payload)
                .getAsJsonObject();
        return json.get("exp").getAsLong();
    }

    private static long now() {
        return new Date().getTime() / 1000;
    }
}
