package com.async.demo.service.impl;

import com.async.demo.service.DemoService;
import com.xy.async.annotation.AsyncExec;
import com.xy.async.enums.AsyncTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class DemoServiceImpl implements DemoService {
    public DemoServiceImpl() {
        log.info("DemoServiceImpl Init...");
    }

    private static AtomicInteger integer = new AtomicInteger(0);

    @Override
    @AsyncExec(type = AsyncTypeEnum.SAVE_SYNC)
    public String invoke(String json) {
        if (integer.getAndIncrement() % 2 == 0) {
            throw new RuntimeException("exception happened...");
        }
        return "OK";
    }
}
