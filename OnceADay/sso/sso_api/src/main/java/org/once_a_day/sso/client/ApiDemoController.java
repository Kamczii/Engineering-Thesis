package org.once_a_day.sso.client;

import org.once_a_day.sso.dto.ExampleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "sso", url = "http://oad-sso/")
public interface ApiDemoController {

    @GetMapping("example")
    ExampleDTO example();

    @GetMapping("plan/default")
    String me();

    @GetMapping("plan/premium")
    String authorized();
}
