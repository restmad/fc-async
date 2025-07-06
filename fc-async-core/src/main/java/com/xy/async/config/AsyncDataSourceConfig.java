package com.xy.async.config;

import java.util.LinkedHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AsyncDataSourceConfig
 *
 * @author xiongyan
 * @date 2022/5/9
 */
@ConfigurationProperties(prefix = "fc.async.datasource")
public class AsyncDataSourceConfig extends LinkedHashMap<String, Object> {

}
