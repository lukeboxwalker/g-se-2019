package de.techfak.gse.lwalkenhorst.apiwrapper;

import uk.co.caprica.vlcj.media.Media;

import java.io.File;

/**
 * Loading a media by its file path.
 * The Media should be prepared to reading its metadata,
 * or additional information.
 */
public interface MediaLoader {
    Media loadMedia(File file);
}
