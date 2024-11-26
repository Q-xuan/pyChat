package com.py.net;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class PyMsg {

    public static final int HEADER_LENGTH = 16;

    int code;
    String cmd;
    Object content;
}
