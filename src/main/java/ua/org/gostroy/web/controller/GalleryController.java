package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.org.gostroy.domain.Album;
import ua.org.gostroy.domain.Image;
import ua.org.gostroy.domain.User;
import ua.org.gostroy.exception.EntityNotFound;
import ua.org.gostroy.exception.HaveNotAccess;
import ua.org.gostroy.exception.NeedAuthorize;
import ua.org.gostroy.service.AlbumService;
import ua.org.gostroy.service.ImageService;
import ua.org.gostroy.service.UserService;
import ua.org.gostroy.web.form.UploadStatus;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by panser on 6/2/2014.
 */
@Controller
@RequestMapping("/gallery/{login}")
public class GalleryController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String root = System.getProperty("user.dir");

    @Autowired(required = true)
    private ImageService imageService;
    @Autowired(required = true)
    private AlbumService albumService;
    @Autowired(required = true)
    private UserService userService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listAlbums(Model model, @PathVariable String login){
        model.addAttribute("login", login);
        model.addAttribute("albums", albumService.findByUserLogin(login));
        return "/gallery/albumList";
    }

    @RequestMapping(value = {"/{albumName}"}, method = RequestMethod.GET)
    public String listImages(Model model, @PathVariable String login, @PathVariable String albumName){
        Album album = albumService.findByUserLoginAndName(login,albumName);
        model.addAttribute("login", login);
        model.addAttribute("album", album);
        model.addAttribute("images", imageService.findByUserLoginAndAlbumName(login, albumName));
        return "/gallery/imageList";
    }

    @RequestMapping(value = {"/{albumName}/{imageName}"}, method = RequestMethod.GET)
    public String viewImage(Model model, HttpServletRequest request,
                            @PathVariable String login, @PathVariable String albumName, @PathVariable String imageName){
        Album album = albumService.findByUserLoginAndName(login,albumName);
        Image image = imageService.findByUserLoginAndAlbumNameAndName(login, albumName, imageName);
        model.addAttribute("login", login);
        model.addAttribute("album", album);
        model.addAttribute("image", image);

        List<Album> albums = albumService.findByUserLogin(login);
        Map<Album, String> albumList = new LinkedHashMap<Album, String>();
//        Map<String,Album> albumList = new LinkedHashMap<String,Album>();
        for(Album album1 : albums){
            albumList.put(album1, album1.getName());
//            albumList.put(album1.getName(), album1);
        }
        model.addAttribute("albumList", albumList);

        String requestURL = request.getRequestURL().toString();
        log.trace("viewImage(), requestURL:" + requestURL);
        model.addAttribute("requestURL", requestURL);
        return "/gallery/imageView";
    }

    @RequestMapping(value = {"/{albumName}/{imageName}/full"}, method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage viewImage(@PathVariable String login, @PathVariable String imageName, @PathVariable String albumName)
            throws HaveNotAccess,NeedAuthorize,EntityNotFound, IOException {
        Album album = albumService.findByUserLoginAndName(login, albumName);
        if(album == null){
            throw new EntityNotFound("Album not found");
        }
        if(!album.isPublicAccess()){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = null;
            if (principal instanceof User) {
                user = (User) principal;
                if(user.getLogin() != login){
                    throw new HaveNotAccess("You have not access to view Image in private Album");
                }
            } else {
                throw new NeedAuthorize("You need to authorize to view private content");
            }
        }
//        log.trace("viewImage(), imageName:" + imageName);
        Image image = imageService.findByUserLoginAndAlbumNameAndName(login, albumName, imageName);
        if(image == null){
            throw new EntityNotFound("Image not found");
        }
//        Resource resource = new ClassPathResource(root + image.getPath() + image.getFile());
//        log.trace("viewImage(), resource:" + resource.getFilename());
//        BufferedImage bufferedImage = ImageIO.read(resource.getURL());
        File filePath = new File(root + image.getPath() + image.getFile());
        log.trace("viewImage(), filePath:" + filePath.toString());
        BufferedImage bufferedImage = ImageIO.read(filePath);
        Graphics g = bufferedImage.getGraphics();
        g.drawString("gostroy.org.ua",20,20);
        return bufferedImage;
    }

    @RequestMapping(value = {"/{albumName}/upload"}, method = RequestMethod.GET)
    public String uploadImagesGET(Model model, @PathVariable String login){
        model.addAttribute("image", new Image());
        return "/gallery/imageUpload";
    }

    @RequestMapping(value = {"/{albumName}/upload"}, method = RequestMethod.POST)
    public String uploadImagesPOST(@Valid @ModelAttribute("image") Image imageFromForm, BindingResult result,
                                   @PathVariable String login, @PathVariable String albumName){
        String viewName;
        if(result.hasErrors()){
            viewName = "/gallery/imageUpload";
        }
        else {
            Image image = new Image();
            User user = userService.findByLogin(login);
            Album album = albumService.findByUserLoginAndName(login, albumName);
            if(album == null){
                album = new Album();
                album.setName(albumName);
                album.setUser(user);
                album.setDefImage(image);
            }

            image.setUser(user);
            image.setAlbum(album);
            if(imageFromForm.getName() == null){
                String originalFilename = imageFromForm.getMultipartFile().getOriginalFilename();
                originalFilename = originalFilename.replace('.','_');
                image.setName(originalFilename);
            }
            else {
                image.setName(imageFromForm.getName());
            }
            image.setDescription(imageFromForm.getDescription());
            image.setMultipartFile(imageFromForm.getMultipartFile());

            UploadStatus uploadStatus = imageService.create(image);
            if (uploadStatus.equals(UploadStatus.EXISTS)){
                result.rejectValue("multipartFile", "error.upload.exists");
                viewName = "/gallery/imageUpload";
            } else if (uploadStatus.equals(UploadStatus.INVALID)){
                result.rejectValue("multipartFile", "error.upload.invalid");
                viewName = "/gallery/imageUpload";
            } else if (uploadStatus.equals(UploadStatus.FAILED)) {
                result.rejectValue("multipartFile", "error.upload.failed");
                viewName = "/gallery/imageUpload";
            } else {
                viewName = "/gallery/" + login + "/" + albumName + "/";
            }
        }
        return viewName;
    }
}
