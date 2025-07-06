package com.xy.async.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.xy.async.constant.AsyncConstant;
import com.xy.async.dto.AsyncExecDto;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步执行提供者
 *
 * @author xiongyan
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "fc.async.kafka", value = "enabled", havingValue = "true")
public class AsyncProducer {

    @Autowired
    private KafkaTemplate<String, AsyncExecDto> kafkaTemplate;

    /**
     * 队列名称前缀：默认是应用名称
     */
    @Value("${fc.async.topic:${spring.application.name:spring-boot-application}}")
    private String asyncTopic;

    /**
     * 发送消息
     *
     * @param asyncExecDto
     * @return
     */
    public boolean send(AsyncExecDto asyncExecDto) {
        String queueName = asyncTopic + AsyncConstant.QUEUE_SUFFIX;
        try {
            log.info("kafka消息开始发送，queueName：'{}', message：{}", queueName, asyncExecDto);
            kafkaTemplate.send(queueName, asyncExecDto);
            log.info("kafka消息发送成功，queueName：'{}', message：{}", queueName, asyncExecDto);
            return true;
        } catch (Exception e) {
            log.error("kafka消息发送失败，queueName：'{}', message：{}", queueName, asyncExecDto, e);
            return false;
        }
    }

}
