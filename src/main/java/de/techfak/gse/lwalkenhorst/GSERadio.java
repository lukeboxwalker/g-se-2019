package de.techfak.gse.lwalkenhorst;

import de.techfak.gse.lwalkenhorst.apiwrapper.VLCJApiWrapper;
import de.techfak.gse.lwalkenhorst.radioplayer.MusicPlayer;
import de.techfak.gse.lwalkenhorst.radioplayer.Playlist;

public final class GSERadio {

    private VLCJApiWrapper apiWrapper;

    private GSERadio() {
        this.apiWrapper = new VLCJApiWrapper();
        Runtime.getRuntime().addShutdownHook(new Thread(this::exit));
    }

    private void start(final String... args) {
        Playlist playlist = new Playlist(args.length == 1 ? args[0] : null, apiWrapper);
        MusicPlayer musicPlayer = new MusicPlayer(apiWrapper);
        musicPlayer.play(playlist);
    }

    private void exit() {
        apiWrapper.release();
    }

    public static void main(final String... args) {
        System.out.println("Started MediaPlayer");
        GSERadio radio = new GSERadio();
        radio.start(args);
    }
}
