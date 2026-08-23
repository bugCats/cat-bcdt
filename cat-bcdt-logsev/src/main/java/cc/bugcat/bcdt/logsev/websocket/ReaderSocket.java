package cc.bugcat.bcdt.logsev.websocket;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.webflux.annotation.WebSocketMapping;
import cc.bugcat.bcdt.logsev.config.AppConfig;
import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.account.AccountSessionFactory;
import cc.bugcat.bcdt.logsev.utils.Tools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


/**
 *
 * @author bugcat-cc
 * */
@WebSocketMapping(ReaderSocket.wsurl)
public class ReaderSocket implements WebSocketHandler {
    
    
    public static final String wsurl = "/logger/server";
    
    
    @Autowired
    private AppConfig appConfig;
    
    @Autowired
    private AccountSessionFactory sessionFactory;

    
    
    public Mono<Void> handle(WebSocketSession session) {
        
        HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        Map<String, String> queryMap = Tools.getQueryMap(handshakeInfo.getUri().getQuery());
        
        String token = queryMap.get("token");
        if (StringUtils.isBlank(token)) {
            return session.close(new CloseStatus(3901, "token不能为空"));
        }
        
        return WebClient.create()
                .get()
                .uri(this.appConfig.getBcdtPath() + "/auth/user/sso?ssoToken=" + token)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume((error) -> {
                    ResultEntity<Void> resp = ResultEntity.fail(Errors.ERROR.code, error.getMessage());
                    return Mono.just(resp.toJson());
                })
                .flatMap(resp -> {
                    JsonTools auths = JsonTools.parseObject(resp);
                    final String errCode = auths.getString("errCode");
                    final String userId = auths.getString("data");
                    
                    if (!"1".equals(errCode) || StringUtils.isBlank(userId)) {
                        return this.loginFail(session, "验证token失败！").and(session.close(CloseStatus.NORMAL));
                    }
                    
                    AccountSession reader = this.sessionFactory.openSession(userId);
                    reader.setWebSocketSession(session);
                    
                    Mono<Void> input = session.receive()
                            .flatMap(message -> reader.receive(message.getPayloadAsText()))
                            .then();
                    
                    Mono<Void> output = session.send(Flux.create(reader::setSinkHandle));
                    
                    return this.loginSuccess(session)
                            .and(Mono.when(input, output).then(Mono.fromRunnable(reader::close)));
                    
                });
    }

    
    
    private Mono<Void> loginSuccess(WebSocketSession session) {
        WebSocketMessage message = session.textMessage(ReaderSocket.build(ReplyMethod.loginSuccess));
        return session.send(Flux.create(sink -> sink.next(message)));
    }

    
    
    private Mono<Void> loginFail(WebSocketSession session, String errMsg) {
        ResultEntity<Void> resp = ResultEntity.fail("3901", Errors.SYS_ERR.code, errMsg);
        WebSocketMessage message = session.textMessage(ReaderSocket.build(ReplyMethod.loginError, resp));
        return session.send(Flux.create(sink -> sink.next(message)));
    }

    
    
    public static String build(ReplyMethod method) {
        return ReaderSocket.build(method, null);
    }
    
    public static String build(ReplyMethod method, Object body) {
        JsonTools head = new JsonTools();
        head.put("type", method.name());
        JsonTools payload = new JsonTools();
        payload.put("head", head);
        payload.put("body", body);
        return payload.toJson();
    }

}

