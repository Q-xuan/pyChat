package com.py.eventBus.chat;

import com.alibaba.fastjson2.JSON;
import com.py.entity.ChatMsg;
import com.py.net.PyMsg;
import com.py.core.BaseHandler;
import com.py.eventBus.EventConstant;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

public class MessageHandler extends BaseHandler<ChatMsg> {

    public MessageHandler(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public String address() {
        return EventConstant.userMessage;
    }

    @Override
    public Handler<Message<ChatMsg>> consumer() {
        return msg -> {
            ChatMsg body = msg.body();
            if (body != null) {
                System.out.println(body.getContent());
            }
            PyMsg content = PyMsg.builder().cmd(getAddress()).content(body).build();
            eventBus.publish(EventConstant.broadcast, JSON.toJSONString(content));
        };
    }
}
