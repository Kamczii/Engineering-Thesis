package org.once_a_day.sso.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.sso.repository.FileRepository;
import org.once_a_day.sso.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class ProfileAdvice {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Before("execution(* org.once_a_day.sso.service.ProfilePictureService.changeProfilePicture(..)) " +
            "&& args(userId,fileId)")
    public void beforeChangeProfilePicture(Long userId, Long fileId) {
        final var profile = fetchUserById(userId);
        Optional.ofNullable(profile.getAvatar())
                        .map(FileDetails::getId)
                        .ifPresent(fileRepository::deleteById);
    }

    private User fetchUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }
}
