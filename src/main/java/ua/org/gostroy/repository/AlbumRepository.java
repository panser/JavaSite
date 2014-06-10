package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.gostroy.model.Album;

import java.util.List;

/**
 * Created by panser on 6/3/2014.
 */
public interface AlbumRepository  extends JpaRepository<Album, Long> {
    List<Album> findByUserLogin(String userLogin);
    Album findByUserLoginAndName(String userLogin, String name);
}
