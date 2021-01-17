package com.maochd.websocket.tool;

import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author maochd
 */
@Component
public class ConnectInfoManagementTool {
    private final static Map<Session, String> sessionMap = new ConcurrentHashMap<>();

    public static void addSingleConnect(String id, Session session) {
        sessionMap.put(session, id);
    }

    public static void addMultipleConnects(Map<Session, String> multipleConnectMap) {
        sessionMap.putAll(multipleConnectMap);
    }

    public static void removeSingleConnect(Session session) {
        sessionMap.remove(session);
    }

    public static void removeMultipleConnects(List<Session> sessions) {
        sessions.forEach(sessionMap::remove);
    }

    public static void removeAllConnects() {
        sessionMap.clear();
    }

    public static String getIdBySession(Session session) {
        return sessionMap.get(session);
    }

    public static Session getSessionById(String value) {
        for (Map.Entry<Session, String> entry : sessionMap.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Map<Session, String> getSessionMap() {
        return sessionMap;
    }
}
