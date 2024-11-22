package com.py.eventBus.user;

import com.py.db.DataMgr;
import com.py.entity.User;
import com.py.eventBus.AbsEvent;
import com.py.eventBus.EventConstant;
import com.py.net.PyMsg;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.Map;

/**
 * @Author py
 * @Date 2024/11/22
 */
public class UserJoinEvent extends AbsEvent<User> {

    public UserJoinEvent(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public String getAddress() {
        return EventConstant.join_inbound;
    }

    @Override
    public Handler<Message<User>> getConsumer() {
        return msg -> {
            User user = msg.body();
            Map<String, User> userMap = DataMgr.getInstance().getUserMap();
            userMap.computeIfAbsent(user.getId(), k -> user);
            msg.reply(user);
        };
    }
}
