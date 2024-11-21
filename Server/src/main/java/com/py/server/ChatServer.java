package com.py.server;

import com.alibaba.fastjson2.JSON;
import com.py.eventBus.AbsEvent;
import com.py.kit.ClassKit;
import com.py.net.PyMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author py
 * @Date 2024/11/21
 */
@Slf4j
public class ChatServer extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        HttpServerOptions ops = new HttpServerOptions();


        EventBus eventBus = vertx.eventBus();
        Set<Class<?>> classes = ClassKit.scanPackage("com.py.eventBus");
        Set<AbsEvent> events = new HashSet<>();
        classes.forEach(clazz -> {
            if (AbsEvent.class.isAssignableFrom(clazz) && clazz != AbsEvent.class) {
                try {
                    events.add((AbsEvent) clazz.getDeclaredConstructor(EventBus.class).newInstance(eventBus));
                } catch (Exception e) {
                    log.error("newInstance error", e);
                }
            }
        });
        Router router = Router.router(vertx);
        router.route().handler(getHandler());
        server.requestHandler(router);
        server.webSocketHandler(handle -> {
            handle.frameHandler(frame -> {
                System.out.println(frame.textData());
                PyMessage pyMessage = JSON.to(PyMessage.class, frame.textData());
            });
        });

        server.listen(8080).onSuccess(suc -> {
            log.info("server start:{}", suc.actualPort());
        });
    }

    public static CorsHandler getHandler() {
        // 配置允许的源，这里可以指定具体的域名，示例中使用 * 表示任意源（不推荐在生产环境这样用）
        Set<String> allowedHeaders = new HashSet<>(Arrays.asList("Content-Type", "Authorization"));
        Set<HttpMethod> allowedMethods = new HashSet<>(Arrays.asList(HttpMethod.GET, HttpMethod.POST));

        return CorsHandler.create()
                .allowCredentials(true)
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods)
                .addOrigin("*");
    }


}
