package org.once_a_day.messenger.messenger_app.mapper;

public interface Mapper<S, T> {
    T map(S s);
}
