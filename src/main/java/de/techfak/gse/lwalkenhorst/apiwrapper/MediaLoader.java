package de.techfak.gse.lwalkenhorst.apiwrapper;

import uk.co.caprica.vlcj.media.Media;

import java.io.File;

public interface MediaLoader {
    Media loadMedia(File file);
}
