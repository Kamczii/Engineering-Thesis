package org.once_a_day.service;

import java.util.Set;

public interface RekognitionService {

    Set<String> detectLabels(Long fileId);

    Set<String> detectLabels(byte[] image);
}
