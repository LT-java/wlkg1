package com.wlkg.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationAPI {
}
