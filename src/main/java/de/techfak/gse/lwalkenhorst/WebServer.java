package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.jsonparser.JSONParser;
import de.techfak.gse.lwalkenhorst.jsonparser.SerialisationException;
import de.techfak.gse.lwalkenhorst.radioplayer.RadioModel;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class WebServer extends NanoHTTPD {

    private final JSONParser parser;
    private final RadioModel radio;

    public WebServer(final int port, RadioModel radio) throws IOException {
        super(port);
        this.radio = radio;
        this.parser = new JSONParser();
        start(SOCKET_READ_TIMEOUT, false);
       // radio.start();
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            switch (session.getUri()) {
                case "/current-song":
                    return newFixedLengthResponse(Response.Status.OK, "application/json", parser.toJSON(radio.getSong()));
                case "/playlist":
                    return newFixedLengthResponse(Response.Status.OK, "application/json", parser.toJSON(radio.getPlaylist()));
            }
        } catch (SerialisationException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "Hier könnte Ihre Werbung stehen.");
    }
}
