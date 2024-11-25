package com.py.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import io.vertx.core.http.ServerWebSocket;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @Author py
 * @Date 2024/11/22
 */
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    String id;
    String username;
    String isOnline;

    @JSONField(serialize = false)
    ServerWebSocket ws;
}
