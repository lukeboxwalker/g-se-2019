package de.techfak.gse.lwalkenhorst;

public final class GSERadio {

    private static MusicPlayer musicPlayer = new MusicPlayer();

    private GSERadio() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::exit));
    }

    private void start(final String... args) {
        MusicReader musicReader = new MusicReader(args.length == 1 ? args[0] : null);
        Playlist playlist = new Playlist(musicReader);
        musicPlayer.play(playlist);
    }

    private void exit() {
        musicPlayer.release();
    }

    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static void main(final String... args) {
        System.out.println("Started MediaPlayer");
        GSERadio radio = new GSERadio();
        radio.start(args);
    }
}
