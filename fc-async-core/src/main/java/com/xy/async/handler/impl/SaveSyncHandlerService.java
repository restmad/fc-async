package com.xy.async.handler.impl;

import com.xy.async.domain.entity.AsyncReq;
import com.xy.async.enums.AsyncTypeEnum;
import com.xy.async.enums.ExecStatusEnum;
import com.xy.async.handler.context.AsyncContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 先保存数据库再异步消息处理
 *
 * @author xiongyan
 * @date 2021/11/17
 */
@Slf4j
@Component
public class SaveSyncHandlerService extends AbstractHandlerService {

    @Override
    public List<String> listType() {
        return Collections.singletonList(AsyncTypeEnum.SAVE_SYNC.name());
    }

    @Override
    public boolean execute(AsyncContext context) {
        // 保存数据库
        AsyncReq asyncReq = this.saveAsyncReq(context.getAsyncExecDto(), ExecStatusEnum.INIT.getStatus());
        try {
            // 同步处理
            context.getJoinPoint().proceed();
            // 更新状态为成功
            asyncReqService.updateStatus(asyncReq.getId(), ExecStatusEnum.SUCCESS.getStatus());
        } catch (Throwable e) {
            log.warn("先同步处理失败：{}", context.getAsyncExecDto(), e);
            // 更新状态为失败
            asyncReqService.updateStatus(asyncReq.getId(), ExecStatusEnum.ERROR.getStatus());
        }

        return true;
    }
}
