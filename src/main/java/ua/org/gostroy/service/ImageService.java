package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import ua.org.gostroy.model.Image;
import ua.org.gostroy.model.User;
import ua.org.gostroy.repository.ImageRepository;
import ua.org.gostroy.web.form.UploadStatus;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by panser on 5/27/2014.
 */
@Service
@Transactional
public class ImageService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String root = System.getProperty("user.dir");

    @Autowired
    ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public Image find(Long id) {
        log.trace("find ...");
        log.trace("find ...");
        Image image = imageRepository.findOne(id);
        log.trace("find.");
        return image;
    }

    @Transactional(readOnly = true)
    public List<Image> findByUserLogin(String userLogin) {
        log.trace("findByUserLogin ...");
        List<Image> images = imageRepository.findByUserLogin(userLogin);
        log.trace("findByUserLogin.");
        return images;
    }

    @Transactional(readOnly = true)
//    @Cacheable(value = "images")
    public List<Image> findByUserLoginAndAlbumName(String userLogin, String albumName) {
        log.trace("findByUserLoginAndAlbumName ...");
        List<Image> images = imageRepository.findByUserLoginAndAlbumName(userLogin, albumName);
        log.trace("findByUserLoginAndAlbumName.");
        return images;
    }

    @Transactional(readOnly = true)
    public Image findByUserLoginAndAlbumNameAndName(String userLogin, String albumName, String name) {
        log.trace("findByUserLoginAndAlbumNameAndName ...");
        Image image = imageRepository.findByUserLoginAndAlbumNameAndName(userLogin, albumName, name);
        log.trace("findByUserLoginAndAlbumNameAndName.");
        return image;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "images")
    public List<Image> findAll() {
        log.trace("findAll ...");
        List<Image>  images = imageRepository.findAll();
        log.trace("findAll.");
        return images;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "images")
    public List<Image> findByPage(Integer page, Integer number) {
        log.trace("findByPage ...");
        PageRequest page1 = new PageRequest(page,number, new Sort(
                new Sort.Order(Sort.Direction.ASC, "createDate"),
                new Sort.Order(Sort.Direction.DESC, "name")
        ));
        Page<Image> images = imageRepository.findAll(page1);
        log.trace("findByPage.");
        return images.getContent();
    }

    @PreAuthorize("#image.user.login == authentication.name or hasRole('ROLE_ADMIN')")
//    @PreAuthorize("isAuthenticated()")
    @Transactional(rollbackFor = Exception.class)
//    @CacheEvict(value = "images")
    public UploadStatus create(Image image) throws NoSuchAlgorithmException, IOException{
        log.trace("create ...");

        log.trace(String.format("create(), root: %s", root));
        log.trace(String.format("create(), image.getOriginalFilename().getName(): %s", image.getMultipartFile().getOriginalFilename()));
        if (image.getMultipartFile() == null || root == null){
            log.trace("create(), UploadStatus.INVALID1");
            return UploadStatus.INVALID;
        }

        StringBuffer target = new StringBuffer();
        target.append(File.separator);
        target.append("upload");
        target.append(File.separator);
        target.append("image");
        target.append(File.separator);

        User user = image.getUser();
        if(user != null) {
            target.append(user.getLogin());
            target.append(File.separator);
        }

        String path = target.toString();
        target.insert(0, this.root);

        File dir = new File(target.toString());
        if (!dir.exists()){
            dir.mkdirs();
        }

        byte[] bytesOfMessage = image.getMultipartFile().getBytes();
        String digest = DigestUtils.md5DigestAsHex(bytesOfMessage);
        log.trace("create(), fileName: " + digest);


        target.append(digest);
        log.trace(String.format("create(), target: %s", target));

        File targetFile = new File(target.toString());
        if (targetFile.exists()){
            log.trace("create(), UploadStatus.EXISTS");
            return UploadStatus.EXISTS;
        }

        try{
            if (ImageIO.read(image.getMultipartFile().getInputStream()) == null ){
                log.trace("create(), UploadStatus.INVALID2");
                return UploadStatus.INVALID;
            }
        }catch (IOException e){
            e.printStackTrace();
            log.trace("create(), UploadStatus.FAILED1");
            return UploadStatus.FAILED;
        }

        try{
            FileCopyUtils.copy(image.getMultipartFile().getBytes(), targetFile);
        }catch(IOException e){
            e.printStackTrace();
            log.trace("create(), UploadStatus.FAILED2");
            return UploadStatus.FAILED;
        }

        image.setSize(image.getMultipartFile().getSize());
        image.setPath(path);
        image.setDigest(digest);
        String imageName = image.getMultipartFile().getOriginalFilename().replace('.', '_');
        // NEED VALIDATE ON imageName
        image.setName(imageName);

        imageRepository.save(image);
        log.trace("create.");
        log.trace("create(), UploadStatus.SUCCESS");
        return UploadStatus.SUCCESS;
    }

    @PreAuthorize("#image.user.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
//    @CacheEvict(value = "images")
    public Long update(Image image) {
        log.trace("update ...");
        imageRepository.save(image);
        log.trace("update.");
        return image.getId();
    }

    @PreAuthorize("#image.user.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
//    @CacheEvict(value = "images")
    public void delete(Image image) {
        log.trace("delete ...");

        StringBuffer target = new StringBuffer();
        target.append(this.root);
        target.append(image.getPath());
        target.append(image.getDigest());
        File targetFile = new File(target.toString());
        log.trace("delete(), targetFile: " + targetFile.toString());
        targetFile.delete();

        imageRepository.delete(image);
        log.trace("delete.");
    }
}
