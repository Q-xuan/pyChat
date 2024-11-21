package com.py.eventBus;

import com.py.net.PyMessage;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbsEvent {

    protected final EventBus eventBus;

    public AbsEvent(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public abstract String getAddress();

    public abstract Handler<Message<PyMessage>> getConsumer();

    public abstract String inbound();

    public abstract String outbound();

    public void register() {
        eventBus.consumer(getAddress(), getConsumer());
        log.info("register success - {}", getAddress());
    }
}
