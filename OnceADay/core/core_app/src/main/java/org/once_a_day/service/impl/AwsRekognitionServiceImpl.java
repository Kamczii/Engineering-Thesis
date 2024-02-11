package org.once_a_day.service.impl;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.LabelCategory;
import org.once_a_day.service.FileStorageService;
import org.once_a_day.service.RekognitionService;
import org.once_a_day.repository.LabelCategoryRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Profile("aws")
public class AwsRekognitionServiceImpl implements RekognitionService {
    private final AmazonRekognition amazonRekognition;
    private final LabelCategoryRepository labelCategoryRepository;
    private final FileStorageService fileStorageService;
    @Override
    public Set<String> detectLabels(final Long fileId) {
        final var file = fileStorageService.downloadImage(fileId);
        final var image = new Image().withBytes(ByteBuffer.wrap(file));
        return extractLabels(image);
    }

    @Override
    public Set<String> detectLabels(final byte[] file) {
        final var image = new Image().withBytes(ByteBuffer.wrap(file));
        return extractLabels(image);
    }

    private Set<String> extractLabels(final Image image) {
        final var supportedCategories = labelCategoryRepository.findAll().stream()
                .map(LabelCategory::getLabel)
                .collect(Collectors.toSet());
        final var detectLabelsRequest = buildRequestObject(image, supportedCategories);
        final var detectLabelsResult = amazonRekognition.detectLabels(detectLabelsRequest);
        return map(detectLabelsResult.getLabels());
    }

    private Set<String> map(List<Label> labels) {
        return labels.stream()
                .map(Label::getName)
                .collect(Collectors.toSet());
    }

    private static DetectLabelsRequest buildRequestObject(final Image image, final Set<String> supportedCategories) {
        final var detectLabelsRequest = new DetectLabelsRequest();
        final var detectLabelsSettings = new DetectLabelsSettings();
        final var generalLabelsSettings = new GeneralLabelsSettings();
        generalLabelsSettings.setLabelCategoryInclusionFilters(supportedCategories);
        detectLabelsSettings.setGeneralLabels(generalLabelsSettings);
        detectLabelsRequest.setSettings(detectLabelsSettings);
        detectLabelsRequest.setImage(image);
        return detectLabelsRequest;
    }


}
