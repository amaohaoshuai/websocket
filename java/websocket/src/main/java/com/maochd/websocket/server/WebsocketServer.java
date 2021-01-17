package com.maochd.websocket.server;

import com.alibaba.fastjson.JSONObject;
import com.maochd.websocket.model.MessageModel;
import com.maochd.websocket.tool.ConnectInfoManagementTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author maochd
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{id}")
public class WebsocketServer {

    private Session currentSession;

    public WebsocketServer() {
        log.info("当前的连接用户: =>{}",
                ConnectInfoManagementTool.getSessionMap().entrySet().toArray().toString());
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("用户{}连接成功", id);
        ConnectInfoManagementTool.getSessionMap();
        ConnectInfoManagementTool.addSingleConnect(id, session);
        currentSession = session;
    }

    @OnClose
    public void onClose() {
        log.info("用户{}关闭连接", ConnectInfoManagementTool.getIdBySession(currentSession));
        ConnectInfoManagementTool.removeSingleConnect(currentSession);
    }

    @OnMessage
    public void onMessage(String dataStr, Session session) {

        // JSON解析dataStr,获取接收方的id和message
        MessageModel messageModel = JSONObject.parseObject(dataStr, MessageModel.class);
        log.info("收到用户{}发给用户{}的信息，发送内容为=>{}", messageModel.getFromId(), messageModel.getToId(), messageModel.getMessage());
        // 发送消息
        sendMessage(session, messageModel.getToId(), messageModel.getMessage());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        sendMessage(session, null, "服务器错误，请联系管理员");
        log.error("当前用户{}操作发生错误，错误内容为=>{}", ConnectInfoManagementTool.getIdBySession(session), error.getMessage());
    }

    public void sendMessage(Session session, String id, String message) {
        Session toSession = ConnectInfoManagementTool.getSessionById(id);
        if (toSession == null) {
            log.error("当前用户{}未在线，发送失败，发送内容为=>{}", id, message);
            session.getAsyncRemote().sendText("当前用户" + id + "未在线，发送失败");
        } else {
            toSession.getAsyncRemote().sendText(message);
            session.getAsyncRemote().sendText("发送成功");
            log.info("用户{}发送给用户{}消息成功，发送内容为=>{}", ConnectInfoManagementTool.getIdBySession(session), id, message);
        }
    }

}
