package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.gostroy.domain.Image;

import java.util.List;

/**
 * Created by panser on 6/2/2014.
 */
public interface ImageRepository  extends JpaRepository<Image, Long> {
    List<Image> findByUserLogin(String userLogin);
    List<Image> findByAlbumName(String albumName);
    List<Image> findByUserLoginAndAlbumName(String userLogin, String albumName);
    Image findByUserLoginAndAlbumNameAndName(String userLogin, String albumName, String name);
}
