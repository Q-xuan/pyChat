package com.py.net;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PyMessage {
    String id;
    String content;
    String sender;
    String channelId;
}
