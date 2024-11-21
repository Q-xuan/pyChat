package com.py.eventBus.chat;

import com.py.net.PyMessage;
import com.py.eventBus.AbsEvent;
import com.py.eventBus.EventConstant;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

public class MessageEvent extends AbsEvent {

    public MessageEvent(EventBus eventBus) {
        super(eventBus);
        super.register();
    }

    @Override
    public String getAddress() {
        return inbound();
    }

    @Override
    public Handler<Message<PyMessage>> getConsumer() {
        return msg -> {
            System.out.println(1123);
        };
    }


    @Override
    public String inbound() {
        return EventConstant.message_inbound;
    }

    @Override
    public String outbound() {
        return EventConstant.message_outbound;
    }

    @Override
    public void register() {
        super.register();
    }
}
