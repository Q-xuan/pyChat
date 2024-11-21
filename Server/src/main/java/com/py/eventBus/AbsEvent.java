package com.py.eventBus;

import com.py.entity.PyMessage;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.bridge.PermittedOptions;

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
    }
}
