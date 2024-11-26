package com.py.server;

import com.alibaba.fastjson2.JSON;
import com.py.db.DataMgr;
import com.py.entity.User;
import com.py.core.BaseHandler;
import com.py.core.kit.ClassKit;
import com.py.net.PyMsg;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author py
 * @Date 2024/11/21
 */
@Slf4j
public class ChatServer extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer(initOps());
        EventBus eventBus = vertx.eventBus();
        //初始化handler
        initHandler(eventBus);
        Router router = initRouter();
        server.requestHandler(router);
        server.listen(8080).onSuccess(suc -> {
            log.info("server start:{}", suc.actualPort());
        });
    }

    private Router initRouter() {
        Router router = Router.router(vertx);
        EventBus eventBus = vertx.eventBus();
        router.route().handler(getHandler());

        List<ServerWebSocket> sockets = new ArrayList<>();

        router.route("/ws").handler(route -> {
            HttpServerRequest request = route.request();
            Future<ServerWebSocket> webSocket = request.toWebSocket();
            webSocket.onSuccess(ws -> {
                log.info("new conn :{},id:{}", ws.remoteAddress(), ws.textHandlerID());
                ws.frameHandler(frame -> {
                    if (frame.isText()) {
                        handlerText(ws, frame, route);
                        sockets.add(ws);
                    }
                });
            });
        });

        eventBus.consumer("broadcast", msg -> {
            log.info("broadcast:{}", msg.body());
            sockets.forEach(ws -> {
                ws.writeTextMessage((String) msg.body());
            });
        });
        return router;
    }

    private static HttpServerOptions initOps() {
        HttpServerOptions ops = new HttpServerOptions();
        ops.setIdleTimeout(120);
        ops.setRegisterWebSocketWriteHandlers(true);
        return ops;
    }

    private static void initHandler(EventBus eventBus) {
        Set<Class<?>> classes = ClassKit.scanPackage("com.py.eventBus");
        classes.forEach(clazz -> {
            if (BaseHandler.class.isAssignableFrom(clazz) && clazz != BaseHandler.class) {
                try {
                    clazz.getDeclaredConstructor(EventBus.class).newInstance(eventBus);
                } catch (Exception e) {
                    log.error("newInstance error", e);
                }
            }
        });
    }

    private static void handlerText(ServerWebSocket handle, WebSocketFrame frame, RoutingContext route) {
        EventBus eventBus = route.vertx().eventBus();
        log.info("ReqData:{}", frame.textData());
        if (!frame.isText()) return;
        PyMsg pyMessage = JSON.to(PyMsg.class, frame.textData());
        if (pyMessage.getCmd().equals("user:join")) {
            //登录
            User user = JSON.to(User.class, pyMessage.getContent());
            DataMgr dataMgr = DataMgr.getInstance();
            user.setWs(handle);
            dataMgr.addUser(user);
            //关闭连接
            handle.closeHandler(ar -> {
                dataMgr.removeUser(user);
                log.info("close conn:{}", user.getUsername());
            });
        }
        eventBus.request(pyMessage.getCmd(), pyMessage.getContent().toString(),
                reply -> {
                    if (reply.succeeded()) {
                        String resp = (String) reply.result().body();
                        handle.writeTextMessage(resp);
                        log.info("resp - cmd:[{}] resp:[{}]", pyMessage.getCmd(), resp);
                    }
                });
    }

    public static CorsHandler getHandler() {
        // 配置允许的源，这里可以指定具体的域名，示例中使用 * 表示任意源（不推荐在生产环境这样用）
        Set<String> allowedHeaders = new HashSet<>(Arrays.asList("Content-Type", "Authorization"));
        Set<HttpMethod> allowedMethods = new HashSet<>(Arrays.asList(HttpMethod.GET, HttpMethod.POST));

        return CorsHandler.create().allowCredentials(true).allowedHeaders(allowedHeaders).allowedMethods(allowedMethods).addOrigin("*");
    }


}
