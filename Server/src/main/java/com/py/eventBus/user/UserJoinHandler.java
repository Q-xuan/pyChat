package com.py.eventBus.user;

import com.py.db.DataMgr;
import com.py.entity.User;
import com.py.core.BaseHandler;
import com.py.eventBus.EventConstant;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.List;
import java.util.Map;

/**
 * @Author py
 * @Date 2024/11/22
 */
public class UserJoinHandler extends BaseHandler<User> {

    public UserJoinHandler(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public String address() {
        return EventConstant.userJoin;
    }

    @Override
    public Handler<Message<User>> consumer() {
        return msg -> {
            User user = msg.body();
            msg.headers();
            DataMgr dataMgr = DataMgr.getInstance();
            Map<String, User> userMap = dataMgr.getUserMap();
            userMap.computeIfAbsent(user.getId(), k -> user);
            List<User> users = dataMgr.joinDefault(user);
            msg.reply(users);
        };
    }
}
