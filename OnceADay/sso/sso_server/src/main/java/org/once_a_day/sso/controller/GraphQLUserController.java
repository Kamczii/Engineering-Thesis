package org.once_a_day.sso.controller;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.enums.FileType;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.database.model.LabelWeight;
import org.once_a_day.database.model.User;
import org.once_a_day.sso.mapper.FileMapper;
import org.once_a_day.sso.repository.FileRepository;
import org.once_a_day.sso.repository.UserRepository;
import org.once_a_day.sso.repository.WeightRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class GraphQLUserController {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final WeightRepository weightRepository;
    private final FileMapper fileMapper;


    @QueryMapping
    Iterable<User> users() {
        return userRepository.findAll();
    }

    @QueryMapping
    User userById(@Argument Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @QueryMapping
    User userByUsername(@Argument String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @SchemaMapping
    public String avatar(User user) {
        return Optional.ofNullable(user.getAvatar())
                .map(FileDetails::getId)
                .map(fileMapper::getUrl)
                .orElse(null);
    }

    @SchemaMapping
    public List<String> activities(User user) {
        return fileRepository.findAllByUserIdAndType(user.getId(), FileType.ACTIVITY_IMAGE)
                .stream()
                .map(FileDetails::getId)
                .map(fileMapper::getUrl)
                .toList();
    }

    @SchemaMapping
    public List<Weight> weights(User user) {
        return weightRepository.findAllByUserIdAndLabelActiveTrueOrderByWeightDesc(user.getId()).stream()
                .limit(15)
                .map(this::map)
                .toList();
    }

    @SchemaMapping
    List<User> matches(User user) {
        return userRepository.findMatchedUser(user.getId());
    }

    private Weight map(LabelWeight weight) {
        return new Weight(weight.getLabel().getLabel(), weight.getLabel().getCategory().getLabel(), weight.getWeight(), null);
    }

    private Specification<User> byId(Long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }
}
