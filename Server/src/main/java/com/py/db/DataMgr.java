package com.py.db;

import com.py.entity.ChatChannel;
import com.py.entity.ChatMsg;
import com.py.entity.User;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author py
 * @Date 2024/11/22
 */
public class DataMgr {


    @Getter
    Map<String, User> userMap = new ConcurrentHashMap<>();
    @Getter
    Map<String, ChatMsg> msgMap = new ConcurrentHashMap<>();
    @Getter
    Map<String, ChatChannel> channelMap = new ConcurrentHashMap<>();
    @Getter
    Map<ChatChannel, List<User>> channelUserMap = new ConcurrentHashMap<>();
    @Getter
    Map<String, String> wsMap = new ConcurrentHashMap<>();


    public User addUser(User user) {
        return userMap.putIfAbsent(user.getId(), user);
    }

    public List<User> joinDefault(User user) {
        ChatChannel aDefault = channelMap.get("default");
        List<User> users = channelUserMap.computeIfAbsent(aDefault, k -> new ArrayList<>());
        users.add(user);
        return users;
    }

    @Getter
    private static final DataMgr instance = new DataMgr();

    private DataMgr() {
        // 私有构造函数，防止外部实例化
        channelMap.put("default", new ChatChannel("default", "General", "通用讨论"));
        channelMap.put("random", new ChatChannel("random", "Random", "随意聊天"));
    }


//    /**
//     * lazyLoading DCL 加 volatile 保证线程安全 和防止指令重排序
//     */
//    private static volatile DataMgr instance;
//
//    public static DataMgr getInstance() {
//        if (instance == null) {
//            synchronized (DataMgr.class) {
//                if (instance == null) {
//                    instance = new DataMgr();
//                }
//            }
//        }
//        return instance;
//    }


    /**
     * 静态内部类加载
     * 使用类加载机制 保证线程安全
     */
//
//     private static class SingletonHolder {
//        private static final DataMgr INSTANCE = new DataMgr();
//    }
//
//    public static DataMgr getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
}
