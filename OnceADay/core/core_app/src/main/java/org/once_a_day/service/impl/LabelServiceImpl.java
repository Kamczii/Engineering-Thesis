package org.once_a_day.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.database.model.LabelWeight;
import org.once_a_day.database.model.RekognitionLabel;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.service.RekognitionService;
import org.once_a_day.repository.FileRepository;
import org.once_a_day.repository.LabelWeightRepository;
import org.once_a_day.repository.RekognitionLabelRepository;
import org.once_a_day.repository.UserRepository;
import org.once_a_day.service.LabelService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final FileRepository fileRepository;
    private final RekognitionService rekognitionService;
    private final RekognitionLabelRepository rekognitionLabelRepository;
    private final LabelWeightRepository labelWeightRepository;
    private final UserRepository userRepository;
    @Override
    public void extractLabelsFromImage(final Long userId, Long fileId) {
        final var labels = getLabels(fileId);
        process(userId, labels);
    }

    @Override
    public void extractLabelsFromImage(final Long userId, final byte[] image) {
        final var labels = getLabels(image);
        process(userId, labels);
    }

    private void process(final Long userId, final Set<String> labels) {
        labels
                .stream()
                .filter(Objects::nonNull)
                .map(this::fetchLabel)
                .map(RekognitionLabel::getId)
                .forEach(labelId -> increment(userId, labelId));
    }

    private Set<String> getLabels(final Long fileId) {
        return rekognitionService.detectLabels(fileId);
    }

    private Set<String> getLabels(final byte[] image) {
        return rekognitionService.detectLabels(image);
    }

    private void increment(Long userId, Long labelId) {
        final LabelWeight labelWeight = labelWeightRepository.findByUserIdAndLabelId(userId, labelId)
                .orElseGet(() -> createWeight(userId, labelId));
        labelWeight.setWeight(labelWeight.getWeight() + 1);
        labelWeightRepository.save(labelWeight);
    }

    private LabelWeight createWeight(final Long userId, final Long labelId) {
        final var user = fetchUser(userId);
        final var label = fetchLabel(labelId);
        final LabelWeight build = LabelWeight.builder()
                .user(user)
                .label(label)
                .weight(0L)
                .build();
        return labelWeightRepository.save(build);
    }

    private RekognitionLabel fetchLabel(final Long labelId) {
        return rekognitionLabelRepository.findById(labelId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private User fetchUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private FileDetails fetchFile(final Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private RekognitionLabel fetchLabel(final String label) {
        return rekognitionLabelRepository.findByLabel(label)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }
}
