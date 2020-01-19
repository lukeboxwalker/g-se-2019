package de.techfak.gse.lwalkenhorst.server;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;

import java.io.IOException;
import java.util.List;

public class ServerSocket extends NanoWSD.WebSocket {

    private List<ServerSocket> sockets;

    public ServerSocket(final NanoHTTPD.IHTTPSession handshakeRequest, final List<ServerSocket> sockets) {
        super(handshakeRequest);
        this.sockets = sockets;
    }

    @Override
    protected void onOpen() {
        sockets.add(this);
    }


    @Override
    protected void onClose(final NanoWSD.WebSocketFrame.CloseCode closeCode, final String s, final boolean b) {
        sockets.remove(this);
    }

    @Override
    protected void onMessage(final NanoWSD.WebSocketFrame webSocketFrame) {

    }

    @Override
    protected void onPong(final NanoWSD.WebSocketFrame webSocketFrame) {

    }

    @Override
    protected void onException(final IOException e) {

    }
}