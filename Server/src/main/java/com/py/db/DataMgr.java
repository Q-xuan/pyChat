package com.py.db;

import com.py.entity.ChatChannel;
import com.py.entity.ChatMsg;
import com.py.entity.User;
import lombok.Data;
import lombok.Getter;

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
    private static final DataMgr instance = new DataMgr();

    private DataMgr() {
        // 私有构造函数，防止外部实例化
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
