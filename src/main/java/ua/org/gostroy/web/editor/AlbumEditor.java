package ua.org.gostroy.web.editor;

import ua.org.gostroy.domain.Album;
import ua.org.gostroy.service.AlbumService;

import java.beans.PropertyEditorSupport;

/**
 * Created by panser on 6/5/2014.
 */
public class AlbumEditor extends PropertyEditorSupport {
    private final AlbumService albumService;
    private final String login;

    public AlbumEditor(AlbumService albumService, String login) {
        this.albumService = albumService;
        this.login = login;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Album album = albumService.findByUserLoginAndName(login,text);
        setValue(album);
    }
}
