package com.py.eventBus.chat;

import com.alibaba.fastjson2.JSON;
import com.py.entity.ChatMsg;
import com.py.net.PyMsg;
import com.py.eventBus.AbsEvent;
import com.py.eventBus.EventConstant;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

public class MessageEvent extends AbsEvent<ChatMsg> {

    public MessageEvent(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public String getAddress() {
        return EventConstant.message_inbound;
    }

    @Override
    public Handler<Message<ChatMsg>> getConsumer() {
        return msg -> {
            ChatMsg body = msg.body();
            if (body != null) {
                System.out.println(body.getContent());
            }
            msg.reply(body.getContent());
        };
    }

    @Override
    public void register() {
        super.register();
    }
}
