package org.once_a_day.sso.controller;

import lombok.RequiredArgsConstructor;
import org.once_a_day.sso.service.ExampleService;
import org.once_a_day.sso.client.ApiDemoController;
import org.once_a_day.sso.dto.ExampleDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class DemoController implements ApiDemoController {
    private final ExampleService exampleService;

    @Override
    @GetMapping("example")
    public ExampleDTO example() {
        return exampleService.example();
    }

    @Override
    @GetMapping("plan/default")
    @PreAuthorize("hasRole('default_user')")
    public String me() {
        return "Default pricing plan.";
    }

    @Override
    @GetMapping("plan/premium")
    @PreAuthorize("hasRole('premium_user')")
    public String authorized() {
        return "Premium pricing plan.";
    }
}
