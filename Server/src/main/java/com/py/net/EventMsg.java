package com.py.net;

import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.impl.EventBusImpl;
import io.vertx.core.eventbus.impl.MessageImpl;

/**
 * @Author py
 * @Date 2024/11/22
 */
public class EventMsg<U, V> extends MessageImpl<U, V> {

    public EventMsg(EventBusImpl bus) {
        super(bus);
    }

    public EventMsg(EventBusImpl bus, U obj) {
        super(bus);
        super.sentBody = obj;
    }

    public EventMsg(String address, MultiMap headers, U sentBody, MessageCodec<U, V> messageCodec, boolean send, EventBusImpl bus) {
        super(address, headers, sentBody, messageCodec, send, bus);
    }

    protected EventMsg(MessageImpl<U, V> other) {
        super(other);
    }

}
