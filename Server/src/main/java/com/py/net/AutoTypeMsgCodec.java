package com.py.net;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @Author py
 * @Date 2024/11/22
 */
public class AutoTypeMsgCodec<R> implements MessageCodec<String, R> {
    @Override
    public void encodeToWire(Buffer buffer, String s) {

    }

    @Override
    public R decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    @Override
    public R transform(String s) {
        return JSON.parseObject(s, new TypeReference<R>() {
        });
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
