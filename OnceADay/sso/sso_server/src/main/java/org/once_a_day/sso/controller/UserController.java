package org.once_a_day.sso.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
import org.once_a_day.sso.service.ProfilePictureService;
import org.once_a_day.sso.service.UserService;
import org.once_a_day.sso.dto.CreateUserRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    //TODO embedded keycloak

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody UUID ssoId) {
        userService.create(ssoId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@PathVariable Long userId) {
        throw new NotImplementedException();
    }

    @PostMapping("create")
    public ResponseEntity<Long> create(@RequestBody CreateUserRequestDTO user) {
        final var userId = userService.create(user);
        return ResponseEntity.ok(userId);
    }
}
