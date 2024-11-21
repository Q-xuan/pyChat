package com.py.eventBus;

public class EventConstant {

    //message
    public static final String message_inbound = "user:message";
    public static final String message_outbound = "message";

    //channel
    public static final String join_inbound = "user:join";
    public static final String join_outbound = "user:joined";
    public static final String switch_inbound = "channel:switch";
    public static final String leave_outbound = "user:left";
    public static final String channelCreate_inbound = "channel:create";
    public static final String channelCreate_outbound = "channel:created";

}
