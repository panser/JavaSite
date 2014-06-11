package ua.org.gostroy.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.gostroy.model.Image;

import java.util.List;

/**
 * Created by panser on 6/2/2014.
 */
public interface ImageRepository  extends JpaRepository<Image, Long> {
    List<Image> findByUserLogin(String userLogin);
    List<Image> findByUserLoginAndAlbumName(String userLogin, String albumName) throws DataAccessException;
    Image findByUserLoginAndAlbumNameAndName(String userLogin, String albumName, String name);
}
