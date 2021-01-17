package com.maochd.websocket.model;

import lombok.Data;

/**
 * @author maochd
 */
@Data
public class MessageModel {
    private String fromId;
    private String toId;
    private String message;
}
