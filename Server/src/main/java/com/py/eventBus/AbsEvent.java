package com.py.eventBus;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.py.db.DataMgr;
import com.py.net.EventMsg;
import com.py.net.PyMsg;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.impl.CodecManager;
import io.vertx.core.eventbus.impl.EventBusImpl;
import io.vertx.core.eventbus.impl.MessageImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
            T event = parseJsonToType(msg.body(), getGenericType());
            Message<T> newMsg = copyMsg(msg, event);
            getConsumer().handle(newMsg);
        };
    }

    private T parseJsonToType(String json, Type type) {
        if (type instanceof ParameterizedType) {
            return JSON.parseObject(json, new TypeReference<T>(type) {
            });
        } else {
            // 处理其他类型的 Type，例如 Class
            return JSON.parseObject(json, type);
        }
    }

    private Type getGenericType() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            return parameterizedType.getActualTypeArguments()[0];
        }
        throw new RuntimeException("Unable to determine generic type");
    }

    private Message<T> copyMsg(Message<String> msg, T event) {
        return new Message<T>() {
            @Override
            public String address() {
                return msg.address();
            }

            @Override
            public MultiMap headers() {
                return msg.headers();
            }

            @Override
            public T body() {
                return event;
            }

            @Override
            public String replyAddress() {
                return msg.replyAddress();
            }

            @Override
            public boolean isSend() {
                return msg.isSend();
            }

            @Override
            public void reply(Object message, DeliveryOptions options) {
                PyMsg output = PyMsg.builder().cmd(getAddress()).content(message).build();
                msg.reply(JSON.toJSONString(output), options);
            }

            @Override
            public <R> Future<Message<R>> replyAndRequest(Object message, DeliveryOptions options) {
                return msg.replyAndRequest(message, options);
            }
        };
    }

    public abstract Handler<Message<T>> getConsumer();

    public void register() {
        eventBus.consumer(getAddress(), baseConsumer());
        log.info("register success - {}", getAddress());
    }
}
