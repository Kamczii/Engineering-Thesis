package org.once_a_day.service;

public interface LabelService {
    void extractLabelsFromImage(final Long userId, Long fileId);

    void extractLabelsFromImage(Long currentUserId, byte[] image);
}
