package com.py.server;

import com.alibaba.fastjson2.JSON;
import com.py.eventBus.AbsEvent;
import com.py.kit.ClassKit;
import com.py.net.AutoTypeMsgCodec;
import com.py.net.PyMsg;
import com.py.net.PyMsgCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.impl.codecs.SerializableCodec;
import io.vertx.core.eventbus.impl.codecs.StringMessageCodec;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;

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
        eventBus.registerCodec(new AutoTypeMsgCodec());
        initEvent(eventBus);
        Router router = Router.router(vertx);
        router.route().handler(getHandler());
        server.requestHandler(router);
        server.webSocketHandler(handle -> {
            log.info("new conn :{},id:{}", handle.remoteAddress(), handle.textHandlerID());
            handle.frameHandler(frame -> {
                handlerText(handle, frame, eventBus);
            });
        });

        server.listen(8080).onSuccess(suc -> {
            log.info("server start:{}", suc.actualPort());
        });
    }

    private static void initEvent(EventBus eventBus) {
        Set<Class<?>> classes = ClassKit.scanPackage("com.py.eventBus");
        classes.forEach(clazz -> {
            if (AbsEvent.class.isAssignableFrom(clazz) && clazz != AbsEvent.class) {
                try {
                    clazz.getDeclaredConstructor(EventBus.class).newInstance(eventBus);
                } catch (Exception e) {
                    log.error("newInstance error", e);
                }
            }
        });
    }

    private static void handlerText(ServerWebSocket handle, WebSocketFrame frame, EventBus eventBus) {
        log.info("ReqData:{}", frame.textData());
        PyMsg pyMessage = JSON.to(PyMsg.class, frame.textData());
        eventBus.request(pyMessage.getCmd(), pyMessage.getContent(), reply -> {
            if (reply.succeeded()) {
                handle.writeTextMessage(JSON.toJSONString(reply.result().body()));
                log.info("resp:{}", pyMessage.getCmd());
            }
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
