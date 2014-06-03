package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.gostroy.domain.Album;
import ua.org.gostroy.domain.Image;
import ua.org.gostroy.domain.User;

import java.util.List;

/**
 * Created by panser on 6/3/2014.
 */
public interface AlbumRepository  extends JpaRepository<Album, Long> {
    List<Album> findByUser(User user);
}
