package org.once_a_day.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.RekognitionLabel;
import org.once_a_day.service.RekognitionService;
import org.once_a_day.repository.RekognitionLabelRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Profile("!aws")
public class RandomRekognitionServiceImpl implements RekognitionService {
    private final RekognitionLabelRepository rekognitionLabelRepository;

    @Override
    public Set<String> detectLabels(final Long fileId) {
        return fetchRandom(10);
    }

    @Override
    public Set<String> detectLabels(final byte[] image) {
        return fetchRandom(10);
    }

    private Set<String> fetchRandom(int n) {
        final long count = rekognitionLabelRepository.count();

        Random rd = new Random(); // creating Random object
        Long[] arr = new Long[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Math.abs(rd.nextLong()) % count; // storing random integers in an array
            System.out.println(arr[i]); // printing each array element
        }

        return Arrays.stream(arr)
                .map(this::fetch)
                .collect(Collectors.toSet());
    }

    private String fetch(long id) {
        return rekognitionLabelRepository.findById(id).map(RekognitionLabel::getLabel).orElse(null);
    }
}
