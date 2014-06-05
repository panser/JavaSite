package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.domain.Album;
import ua.org.gostroy.repository.AlbumRepository;

import java.util.List;

/**
 * Created by panser on 5/27/2014.
 */
@Service
@Transactional
public class AlbumService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String root = System.getProperty("webapp.root");

    @Autowired
    AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    public Album find(Long id) {
        log.trace("find ...");
        Album album = albumRepository.findOne(id);
        log.trace("find.");
        return album;
    }

    @Transactional(readOnly = true)
    public List<Album> findByUserLogin(String userLogin) {
        log.trace("findByUserLogin ...");
        List<Album> albums = albumRepository.findByUserLogin(userLogin);
        log.trace("findByUserLogin.");
        return albums;
    }

    @Transactional(readOnly = true)
    public Album findByUserLoginAndName(String userLogin, String name) {
        log.trace("findByUserLoginAndName ...");
        Album albums = albumRepository.findByUserLoginAndName(userLogin, name);
        log.trace("findByUserLoginAndName.");
        return albums;
    }

    @Transactional(readOnly = true)
    public List<Album> findAll() {
        log.trace("findAll ...");
        List<Album>  albums = albumRepository.findAll();
        log.trace("findAll.");
        return albums;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(Album album) {
        log.trace("create ...");
        albumRepository.save(album);
        log.trace("create.");
        return album.getId();
    }

    @PreAuthorize("#album.user.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public Long update(Album album) {
        log.trace("update ...");
        albumRepository.save(album);
        log.trace("update.");
        return album.getId();
    }

    @PreAuthorize("#album.user.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Album album) {
        log.trace("delete ...");
        albumRepository.delete(album);
        log.trace("delete.");
    }
}
