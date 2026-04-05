package com.usmani.websocket;

public enum ClientState {
    INITIALIZED, //initialized means there is no packet received so far
    HANDSHAKE, //in handshake phase
    READY, //handshake done
    CLOSED, //closed
}
