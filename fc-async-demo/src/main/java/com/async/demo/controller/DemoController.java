package com.async.demo.controller;

import com.async.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("demo")
@Slf4j
public class DemoController {
    @Resource
    private DemoService demoService;

    @RequestMapping("")
    public String index(@RequestBody(required = false) String json) {
        return demoService.invoke(json);
    }
}
