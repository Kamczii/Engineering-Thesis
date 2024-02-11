package org.once_a_day.sso.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.sso.service.ExampleService;
import org.once_a_day.sso.dto.ExampleDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {
    @Override
    public ExampleDTO example() {
        return new ExampleDTO("Hello2");
    }
}
