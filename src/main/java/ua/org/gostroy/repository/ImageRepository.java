package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.gostroy.domain.*;

import java.util.List;

/**
 * Created by panser on 6/2/2014.
 */
public interface ImageRepository  extends JpaRepository<Image, Long> {
    List<Image> findByUser(User user);
    List<Image> findByAlbum(Album album);
}
