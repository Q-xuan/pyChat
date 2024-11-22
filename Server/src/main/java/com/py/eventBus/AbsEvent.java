package com.py.eventBus;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.py.db.DataMgr;
import com.py.net.EventMsg;
import com.py.net.PyMsg;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.impl.CodecManager;
import io.vertx.core.eventbus.impl.EventBusImpl;
import io.vertx.core.eventbus.impl.MessageImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbsEvent<T> {

    protected final EventBus eventBus;

    public AbsEvent(EventBus eventBus) {
        this.eventBus = eventBus;
        register();
    }

    public abstract String getAddress();

    public Handler<Message<String>> baseConsumer() {
        return msg -> {
            T event = JSON.parseObject(msg.body(), new TypeReference<T>() {
            });
            Message<T> newMsg = new Message<>();
            getConsumer().handle(newMsg);
        };
    }

    public abstract Handler<Message<T>> getConsumer();

    public void register() {
        eventBus.consumer(getAddress(), baseConsumer());
        log.info("register success - {}", getAddress());
    }
}
