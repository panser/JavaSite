package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.domain.Album;
import ua.org.gostroy.domain.Image;
import ua.org.gostroy.domain.User;
import ua.org.gostroy.repository.ImageRepository;

import java.util.List;

/**
 * Created by panser on 5/27/2014.
 */
@Service
@Transactional
public class ImageService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String root = System.getProperty("webapp.root");

    @Autowired
    ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public Image find(Long id) {
        log.trace("find ...");
        Image image = imageRepository.findOne(id);
        log.trace("find.");
        return image;
    }

    @Transactional(readOnly = true)
    public List<Image> findByUser(User user) {
        log.trace("findByUser ...");
        List<Image> images = imageRepository.findByUser(user);
        log.trace("findByUser.");
        return images;
    }

    @Transactional(readOnly = true)
    public List<Image> findByAlbum(Album album) {
        log.trace("findByAlbum ...");
        List<Image> images = imageRepository.findByAlbum(album);
        log.trace("findByAlbum.");
        return images;
    }

    @Transactional(readOnly = true)
    public List<Image> findAll() {
        log.trace("findAll ...");
        List<Image>  images = imageRepository.findAll();
        log.trace("findAll.");
        return images;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(Image image) {
        log.trace("create ...");
        imageRepository.save(image);
        log.trace("create.");
        return image.getId();
    }

    @PreAuthorize("#image.user.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public Long update(Image image) {
        log.trace("update ...");
        imageRepository.save(image);
        log.trace("update.");
        return image.getId();
    }

    @PreAuthorize("#image.user.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Image image) {
        log.trace("delete ...");
        imageRepository.delete(image);
        log.trace("delete.");
    }
}
