package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import ua.org.gostroy.model.Album;
import ua.org.gostroy.model.Image;
import ua.org.gostroy.model.User;
import ua.org.gostroy.web.form.UploadStatus;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by panser on 6/16/2014.
 */
@Service
public class GalleryControllerService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private MessageSource messageSource;

    @Autowired
    ImageService imageService;
    @Autowired
    AlbumService albumService;
    @Autowired
    UserService userService;

    public Map<String, String> uploadImages(MultipartRequest multipartRequest, String login, String albumName, Locale locale)
            throws IOException, NoSuchAlgorithmException {
        Map<String, String> errorsMap = new HashMap<String, String>();
        List<MultipartFile> files = multipartRequest.getFiles("files");
        if(files.get(0).isEmpty()){
            errorsMap.put("", messageSource.getMessage("error.upload.notselected", null, locale));
        }
        else {
            User user = userService.findByLogin(login);
            Album album = albumService.findByUserLoginAndName(login, albumName);
            if (album == null) {
                album = new Album();
                album.setName(albumName);
                album.setUser(user);
            }
            for (MultipartFile multipartFile : files) {
                Image image = new Image();
                image.setUser(user);
                image.setAlbum(album);
                image.setMultipartFile(multipartFile);

                UploadStatus uploadStatus = imageService.create(image);
                String fileName = image.getMultipartFile().getOriginalFilename();
                if (uploadStatus.equals(UploadStatus.EXISTS)) {
                    errorsMap.put(fileName, messageSource.getMessage("error.upload.exists", null, locale));
                } else if (uploadStatus.equals(UploadStatus.INVALID)) {
                    errorsMap.put(fileName, messageSource.getMessage("error.upload.invalid", null, locale));
                } else if (uploadStatus.equals(UploadStatus.FAILED)) {
                    errorsMap.put(fileName, messageSource.getMessage("error.upload.failed", null, locale));
                }
            }
        }
        return errorsMap;
    }
}
