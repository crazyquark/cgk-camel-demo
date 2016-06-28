package com.cegeka;

import java.io.Serializable;

import lombok.Data;

@Data
public class Message implements Serializable{
    /**
     * Messages need to be serializable
     */
    private static final long serialVersionUID = -3125448869714774252L;
    private String message;
    
    public Message(String message) {
        this.message = message;
    }
}
