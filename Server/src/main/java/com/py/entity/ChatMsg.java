package com.py.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ChatMsg {
    String id;
    String content;
    String sender;
    String channelId;
}
