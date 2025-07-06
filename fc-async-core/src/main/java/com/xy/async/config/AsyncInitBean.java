package com.xy.async.config;

import com.xy.async.annotation.AsyncExec;
import com.xy.async.dto.ProxyMethodDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 异步执行初始化
 *
 * @author xiongyan
 * @date 2021/6/19
 */
@Component
@ConditionalOnProperty(prefix = "fc.async", value = "enabled", havingValue = "true")
@Slf4j
public class AsyncInitBean implements BeanPostProcessor {
    @Autowired
    private AsyncProxy asyncProxy;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (ArrayUtils.isEmpty(methods)) {
            return bean;
        }
        for (Method method : methods) {
            AsyncExec asyncExec = AnnotationUtils.findAnnotation(method, AsyncExec.class);
            if (null == asyncExec) {
                continue;
            }
            log.info("async enhanced bean: {}", beanName);
            ProxyMethodDto proxyMethodDto = new ProxyMethodDto();
            proxyMethodDto.setBean(bean);
            proxyMethodDto.setMethod(method);
            // 生成方法唯一标识
            String key = asyncProxy.getAsyncMethodKey(bean, method);
            asyncProxy.setProxyMethod(key, proxyMethodDto);
        }
        return bean;
    }
}
