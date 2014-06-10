package ua.org.gostroy.web.editor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.org.gostroy.model.Album;
import ua.org.gostroy.service.AlbumService;

import java.beans.PropertyEditorSupport;

/**
 * Created by panser on 6/5/2014.
 */
public class AlbumEditor extends PropertyEditorSupport {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AlbumService albumService;
    private final String login;

    public AlbumEditor(AlbumService albumService, String login) {
        this.albumService = albumService;
        this.login = login;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        log.trace("setAsText() start ...");
        Album album = albumService.findByUserLoginAndName(login,text);
        setValue(album);
    }
}
