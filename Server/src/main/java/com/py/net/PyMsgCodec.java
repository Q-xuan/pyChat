package com.py.net;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @Author py
 * @Date 2024/11/22
 */
public class PyMsgCodec implements MessageCodec<PyMsg, PyMsg> {

    private static final String DELIMITER = "\n";

    @Override
    public void encodeToWire(Buffer buffer, PyMsg pyMsg) {
        buffer.appendString(pyMsg.getCmd() + DELIMITER + pyMsg.getContent());
    }

    @Override
    public PyMsg decodeFromWire(int pos, Buffer buffer) {
        String message = buffer.getString(pos, buffer.length());
        String[] parts = message.split(DELIMITER, 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid message format");
        }
        String cmd = parts[0];
        String content = parts[1];
        return new PyMsg(cmd, content);
    }

    @Override
    public PyMsg transform(PyMsg pyMsg) {
        return pyMsg;
    }

    @Override
    public String name() {
        return "PyMsgCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
