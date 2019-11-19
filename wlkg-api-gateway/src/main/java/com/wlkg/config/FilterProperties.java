package com.wlkg.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "wlkg.filter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterProperties {


    private List<String> allowPaths;
}
